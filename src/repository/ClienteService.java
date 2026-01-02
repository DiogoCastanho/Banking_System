package src.repository;

import src.models.Cliente;
import src.models.Conta;

public class ClienteService {

    private ClienteCSVRepository Clienterepository = new ClienteCSVRepository();
    private ContaService contaService = new ContaService();

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

        if (Clienterepository.buscarPorNif(nif) != null) {
            throw new IllegalArgumentException("O NIF introduzido já existe.");
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
        Clienterepository.salvar(cliente);

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

        Clienterepository.atualizar(c);

        return c;

    }
}
