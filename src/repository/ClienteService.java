package src.repository;

import src.Cliente;
import src.Conta;
import src.Banco;
import src.Cartao;
import src.repository.ClienteCSVRepository;
import src.utils.Gerador;
import java.util.*;

public class ClienteService {

    private ClienteCSVRepository repository = new ClienteCSVRepository();
    private Banco banco;

    // construtor
    public ClienteService(Banco banco) {
        this.banco = banco;
    }

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

        // gera dados do cartão automaticamente
        Cartao cartao = new Cartao(
                Gerador.gerarNumeroCartao(),
                Gerador.gerarValidade(),
                Gerador.gerarCVV(),
                Gerador.gerarPIN()
        );

        Conta conta = new Conta(
                cartao,
                Gerador.gerarIBAN(),
                0,
                null
        );

        Cliente cliente = new Cliente(
                nome,
                nif,
                utilizador,
                senha,
                conta
        );

        // guardar o cliente
        repository.salvar(cliente);

        return cliente;
    }

    // public List<Cliente> listarClientes() {
    //   return banco.getClientes();
    // }
}
