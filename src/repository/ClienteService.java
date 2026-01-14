package src.repository;

import java.util.List;

import src.utils.*;

import src.models.Cliente;
import src.models.Conta;

public class ClienteService {

    private ClienteCSVRepository clienteRepository = new ClienteCSVRepository();
    private ContaService contaService = new ContaService();
    private ContaCSVRepository contaRepository = new ContaCSVRepository();

    public Cliente criarCliente(
            String nome,
            String nif,
            String utilizador,
            String senha
    ) {

        // validações
        if (!nif.matches("\\d{9}")) {
            throw new IllegalArgumentException("NIF deve ter 9 dígitos");
        }

        if (senha.length() < 4) {
            throw new IllegalArgumentException("Senha muito curta");
        }

        if (clienteRepository.buscarPorNif(nif) != null) {
            throw new IllegalArgumentException("O NIF introduzido já existe.");
        }

        if (clienteRepository.buscarClientePorUtilizador(utilizador) != null) {
            throw new IllegalArgumentException("O utilizador já existe");
        }

        Conta conta = contaService.criarContaDefault(nif);

        Cliente cliente = new Cliente(
                nome,
                nif,
                utilizador,
                senha,
                conta
        );

        // guardar o cliente
        clienteRepository.salvar(cliente);

        return cliente;
    }

    public Cliente editarCliente(String nome, String nif, String utilizador, String senha) {

        Cliente existente = clienteRepository.buscarPorNif(nif);
        if (existente == null) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }

        boolean alterado = false;

        if (!existente.getNome().equals(nome)) {
            existente.setNome(nome);
            alterado = true;
        }

        if (!existente.getUtilizador().equals(utilizador)) {
            Cliente outro = clienteRepository.buscarClientePorUtilizador(utilizador);
            if (outro != null && !outro.getNif().equals(nif)) {
                throw new IllegalArgumentException("O utilizador já existe");
            }
            existente.setUtilizador(utilizador);
            alterado = true;
        }

        if (!existente.getSenha().equals(senha)) {
            if (senha.length() < 4) {
                throw new IllegalArgumentException("Senha muito curta");
            }
            existente.setSenha(senha);
            alterado = true;
        }

        if (!alterado) {
            Utils.aviso("Nenhum dado foi alterado.");
            return existente;
        }

        clienteRepository.atualizar(existente);

        return existente;

    }

    public Cliente removerCliente(String nif) {
        if (!nif.matches("\\d{9}")) {
            throw new IllegalArgumentException("NIF deve ter 9 dígitos");
        }

        Cliente cliente = clienteRepository.buscarPorNif(nif);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }

        List<Conta> contas = contaRepository.listarContas("contas.csv");

        boolean saldoZero = true;
        double saldoTotal = 0;  // Para mostrar quanto tem

        for (Conta c : contas) {
            if (c.getNifCliente().equals(nif)) {
                if (c.getSaldo() > 0) {
                    saldoZero = false;
                    saldoTotal += c.getSaldo();  // Acumula saldo
                }
            }
        }

        if (saldoZero) {
            contaRepository.removerPorNifCliente(nif);
            clienteRepository.removerPorNif(nif);
            Utils.sucesso("Cliente e suas contas removidos com sucesso.");
        } else {
            // Informa o utilizador porque não pode remover
            Utils.erro("Não é possível remover o cliente.");
            Utils.aviso("Saldo total nas contas: " + String.format("%.2f EUR", saldoTotal));
            Utils.aviso("Transfira ou levante o saldo de todas as contas antes de remover o cliente.");
        }

        return cliente;
        
    }
}
