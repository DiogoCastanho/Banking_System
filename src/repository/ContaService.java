package src.repository;

import src.models.Cliente;
import src.models.Conta;
import src.models.TipoContaEnum;

import java.util.Scanner;

import src.models.Cartao;
import src.utils.Gerador;
import src.utils.Utils;

public class ContaService {

    Scanner sc = new Scanner(System.in);
    private ContaCSVRepository repository = new ContaCSVRepository();
    private ClienteCSVRepository clienteRepository = new ClienteCSVRepository();

    public Conta criarContaDefault(String nif) { 

        // gera dados do cartão automaticamente
        Cartao cartao = new Cartao(
                Gerador.gerarNumeroCartao(),
                Gerador.gerarValidade(),
                Gerador.gerarCVV(),
                Gerador.gerarPIN(),
                false
        );

        Conta conta = new Conta(
                Gerador.gerarIBAN(),
                nif,
                0,
                TipoContaEnum.CONTA_ORDEM,
                cartao
        );

        // guardar o cliente
        repository.salvar(conta);

        return conta;
    }

    public Conta criarContaAdicional(String nifCliente) { 

        Cliente cliente = clienteRepository.buscarPorNif(nifCliente);

        if (cliente == null) {
            Utils.erro("Cliente não encontrado");
            return null;
        }

        Utils.sucesso("Cliente encontrado: " + cliente.getNome() + " " + cliente.getUtilizador());

        System.out.println("Escolha o tipo de conta:");
        System.out.println("1 - Conta Ordem");
        System.out.println("2 - Conta Poupança");
        System.out.print("Opção: ");
        int op = sc.nextInt();
        sc.nextLine();

        TipoContaEnum tipoConta = (op == 1) ? TipoContaEnum.CONTA_ORDEM : TipoContaEnum.CONTA_POUPANCA;

        // gera dados do cartão automaticamente
        Cartao cartao1 = new Cartao(
                Gerador.gerarNumeroCartao(),
                Gerador.gerarValidade(),
                Gerador.gerarCVV(),
                Gerador.gerarPIN(),
                false
        );

        Conta conta = new Conta(
                Gerador.gerarIBAN(),
                nifCliente,
                0,
                tipoConta,
                cartao1
        );

        // guardar o cliente
        repository.salvar(conta);

        return conta;
    }

}
