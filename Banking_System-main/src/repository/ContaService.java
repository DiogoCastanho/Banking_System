package src.repository;

import src.models.Cliente;
import src.models.Conta;
import src.models.TipoContaEnum;
import src.ui.ConsolaUi;

import java.util.List;
import java.util.Scanner;

import src.models.Cartao;
import src.utils.Gerador;
import src.utils.Utils;

public class ContaService {

    Scanner sc = new Scanner(System.in);
    private ContaCSVRepository contaRepository = new ContaCSVRepository();
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
        contaRepository.salvar(conta);

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
        contaRepository.salvar(conta);

        return conta;
    }

    
    public Conta escolherConta(List<Conta> contas) {
        int opcao;

        do {
            opcao = Utils.lerOpcao(sc, "Escola a conta: ");
        } while (opcao < 1 || opcao > contas.size());

        return contas.get(opcao - 1);
    }

    
    public Conta buscarContaParaRemover(Conta conta) {

        if (!conta.getNifCliente().matches("\\d{9}")) {
            throw new IllegalArgumentException("NIF deve ter 9 dígitos");
        }

        List<Conta> contas = contaRepository.listarContas("contas.csv");

        boolean saldoZero = true;

        for (Conta c : contas) {
            if (c.getCartao().equals(conta.getCartao())) {
                if (c.getSaldo() > 0) {
                    saldoZero = false;
                    break;
                }
            }

        }

        if (saldoZero) {
            contaRepository.removerPorNifCliente(conta.getNifCliente());
            Utils.sucesso("Remoção efetuada com sucesso.");

        }


        return conta;
        
    }

}
