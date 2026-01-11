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
        int op = Utils.lerInteiroSeguro(sc, "Opção: ");

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

        if (conta.getSaldo() > 0) {
            Utils.erro("Não é possível remover a conta. Saldo atual: " + conta.getSaldo() + " EUR");
            Utils.aviso("Transfira ou levante o saldo antes de remover a conta.");
            return conta;
        }

        List<Conta> contas = contaRepository.listarContas("contas.csv");
        List<Conta> contasRestantes = contas.stream()
            .filter(c -> !c.getIban().equals(conta.getIban()))
            .toList();

        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter("contas.csv"))) {
            pw.println("IBAN,NIF,Saldo,TipoConta,NumeroCartao,Validade,CVV,Pin,Bloqueado");
            for (Conta c : contasRestantes) {
                pw.println(c.toCsv());
            }
            Utils.sucesso("Conta " + conta.getIban() + " removida com sucesso.");
        } catch (java.io.IOException e) {
            throw new RuntimeException("Erro ao remover conta: " + e.getMessage());
        }

        return conta;        
    }

    public Conta depositarDinheiro(Conta conta, double valor) {
        if (valor <= 0) {
            Utils.erro("O valor introduzido é inválido");
            return conta;
        }

        conta.depositarDinheiro(valor); // Usa o método da classe Conta
        contaRepository.atualizar(conta);
        
        Utils.sucesso("Depósito realizado com sucesso!");
        System.out.println("Novo saldo: " + conta.getSaldo() + " EUR");
        
        ConsolaUi.pausa(sc);
        return conta;
    }
}