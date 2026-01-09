package src.repository;

import java.util.List;
import java.util.Scanner;

import src.utils.*;

import src.models.Cliente;
import src.models.Conta;
import src.ui.ConsolaUi;

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
        Cliente c = new Cliente(nome, nif, utilizador, senha, null);

        if (!c.getNome().equals(nome)) {
            c.setNome(nome);
        }

        if (!c.getUtilizador().equals(utilizador)) {
            c.setUtilizador(utilizador);
        }

        if (!c.getSenha().equals(senha)) {
            c.setSenha(senha);
        }

        clienteRepository.atualizar(c);

        return c;

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

        for (Conta c : contas) {
            if (c.getNifCliente().equals(nif)) {
                if (c.getSaldo() > 0) {
                    saldoZero = false;
                    break;
                }
            }

        }

        if (saldoZero) {
            contaRepository.removerPorNifCliente(nif);
            clienteRepository.removerPorNif(nif);
            Utils.sucesso("Remoção efetuada com sucesso.");

        }


        return cliente;
        
    }
}
