package src.repository;

import src.models.Cliente;
import src.models.Conta;
import src.models.Cartao;
import src.utils.Gerador;

public class ClienteService {

    private ClienteCSVRepository repository = new ClienteCSVRepository();

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
}
