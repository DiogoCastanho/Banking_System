package src.menus;

import java.util.List;
import java.util.Scanner;

import src.models.CanalAcesso;
import src.models.Cliente;
import src.models.Conta;
import src.repository.ContaCSVRepository;
import src.repository.ContaService;
import src.ui.ConsolaUi;
import src.utils.Session;
import src.utils.Utils;

public class menuBancoCliente {

    public static void showMenuBancoCliente(Cliente cliente) {
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            Utils.limparTela();
            ConsolaUi.titulo("Menu Cliente - " + cliente.getNome());

            System.out.println("1 - Aceder Conta");
            System.out.println("0 - Voltar");

            ConsolaUi.linha();
            opcao = Utils.lerInteiroSeguro(sc, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    acederConta(sc, cliente);
                    break;
                case 0:
                    Utils.sucesso("Logout efetuado. Até breve!");
                    ConsolaUi.pausa(sc);
                    break;
                default:
                    Utils.erro("Opção inválida!");
                    ConsolaUi.pausa(sc);
            }
        } while (opcao != 0);

    }

    private static void acederConta(Scanner sc, Cliente cliente) {
        ContaCSVRepository contaRepo = new ContaCSVRepository();
        List<Conta> contas = contaRepo.buscarContasCliente(cliente.getNif());

        if (contas == null || contas.isEmpty()) {
            Utils.aviso("Não possui contas associadas ao seu perfil.");
            ConsolaUi.pausa(sc);
            return;
        }

        Utils.limparTela();
        ConsolaUi.titulo("Selecionar Conta");
        ConsolaUi.secao("Contas Disponíveis");
        
        for (int i = 0; i < contas.size(); i++) {
            Conta c = contas.get(i);
            System.out.println("[" + (i + 1) + "] " + c.getTipoConta() + " - IBAN: " + c.getIban());
            System.out.println("    Saldo: " + String.format("%.2f EUR", c.getSaldo()));
            ConsolaUi.linha();
        }
        
        System.out.println("[0] Voltar ao Menu Principal");
        ConsolaUi.linha();
        int escolha = Utils.lerInteiroSeguro(sc, "Escolha uma conta pelo número: ");

        if (escolha == 0) {
            return; 
        }

        if (escolha < 1 || escolha > contas.size()) {
            Utils.erro("Opção inválida!");
            ConsolaUi.pausa(sc);
            return;
        }

        Conta contaSelecionada = contas.get(escolha - 1);
        Session.setCurrentConta(contaSelecionada);
        
        menuConta(sc, contaSelecionada, contaRepo);
        
        Session.clearConta();
    }

    private static void menuConta(Scanner sc, Conta conta, ContaCSVRepository contaRepo) {
        int opcao;

        do {
            Utils.limparTela();
            ConsolaUi.titulo("Menu Cliente - Operações");
            
            System.out.println("Conta : " + conta.getIban());
            System.out.println("Tipo  : " + conta.getTipoConta());
            System.out.println("Saldo : " + String.format("%.2f EUR", conta.getSaldo()));
            
            ConsolaUi.secao("Menu de Operações");
            System.out.println("1 - Consultar Saldo");
            System.out.println("2 - Depositar dinheiro");
            System.out.println("3 - Levantar dinheiro");
            System.out.println("4 - Fazer Transferências");
            System.out.println("0 - Sair da Conta");
            
            ConsolaUi.linha();

            opcao = Utils.lerInteiroSeguro(sc, "Escolha uma opção: ");
            sc.nextLine();

            switch (opcao) {
                case 1:
                    CanalAcesso.consultarSaldo(sc, conta);
                    break;
                case 2:
                    ContaService contaService = new ContaService();
                    ConsolaUi.titulo("Depositar dinheiro");

                    System.out.println("Conta : " + conta.getIban());
                    System.out.println("Tipo  : " + conta.getTipoConta());
                    System.out.println("Saldo : " + String.format("%.2f EUR", conta.getSaldo()));

                    ConsolaUi.linha();
                    double valorDeposito = Utils.lerDoubleSeguro(sc, "Valor a depositar: ");

                    Conta depositar = contaService.depositarDinheiro(conta, valorDeposito);

                    break;
                case 3:
                    ConsolaUi.titulo("Levantar Dinheiro");
                    double quantidade = Utils.lerDoubleSeguro(sc, "Quantidade a levantar:");
                    src.utils.Session.getCurrentConta().levantarDinheiro(quantidade);
                    contaRepo.atualizar(src.utils.Session.getCurrentConta());
                    break;
                case 4:
                    ConsolaUi.titulo("Fazer Transferências");
                    CanalAcesso.fazerTransferencia(sc, conta, contaRepo);
                    break;
                case 0:
                    Utils.sucesso("A voltar ao menu principal...");
                    ConsolaUi.pausa(sc);
                    break;
                default:
                    Utils.erro("Opção inválida!");
                    ConsolaUi.pausa(sc);
            }

        } while (opcao != 0);
    }
}
