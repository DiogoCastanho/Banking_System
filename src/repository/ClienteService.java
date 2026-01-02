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

        // if (repository.procurarPorNif(nif)) {
        //     throw new IllegalArgumentException("Cliente já existe");
        // }

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
}
