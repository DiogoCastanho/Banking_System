package src.menus;

import java.util.*;
import src.models.Cliente;
import src.models.Conta;
import src.models.CanalAcesso;
import src.repository.*;
import src.ui.ConsolaUi;
import src.utils.*;

public class menuWebBanking {

    public static void showMenuWebBanking() {
        Scanner sc = new Scanner(System.in);
        ClienteCSVRepository clienteRepo = new ClienteCSVRepository();

        Cliente clienteLogado = realizarLogin(sc, clienteRepo);
        
        if (clienteLogado == null) {
            return;
        }

        menuPrincipal(sc, clienteLogado);
        
        Session.clearCliente();
    }

    private static Cliente realizarLogin(Scanner sc, ClienteCSVRepository clienteRepo) {
        int tentativas = 0;
        final int MAX_TENTATIVAS = 3;

        while (tentativas < MAX_TENTATIVAS) {
            Utils.limparTela();
            ConsolaUi.titulo("WebBanking - Autenticação");
            
            System.out.print("Username: ");
            String username = sc.nextLine().trim();
            
            if (username.equalsIgnoreCase("0") || username.isEmpty()) {
                Utils.aviso("Login cancelado.");
                ConsolaUi.pausa(sc);
                return null;
            }
            
            System.out.print("Password: ");
            String password = sc.nextLine().trim();

            Cliente cliente = clienteRepo.buscarInfoCliente(username, password);
            
            if (cliente != null) {
                Session.setCurrentCliente(cliente);
                Utils.sucesso("Autenticação bem-sucedida! Bem-vindo(a), " + cliente.getNome());
                ConsolaUi.pausa(sc);
                return cliente;
            }

            tentativas++;
            Utils.erro("Credenciais inválidas! Tentativa " + tentativas + "/" + MAX_TENTATIVAS);
            
            if (tentativas >= MAX_TENTATIVAS) {
                Utils.erro("Número máximo de tentativas excedido. Acesso bloqueado temporariamente.");
                ConsolaUi.pausa(sc);
                return null;
            }
            
            ConsolaUi.pausa(sc);
        }
        
        return null;
    }

    private static void menuPrincipal(Scanner sc, Cliente cliente) {
        int opcao;

        do {
            Utils.limparTela();
            ConsolaUi.titulo("WebBanking - " + cliente.getNome());
            
            System.out.println("1 - Aceder à Conta");
            System.out.println("2 - Alterar Password");
            System.out.println("0 - Logout");
            
            ConsolaUi.linha();
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();
            sc.nextLine(); 

            switch (opcao) {
                case 1:
                    acederConta(sc, cliente);
                    break;
                case 2:
                    alterarPassword(sc, cliente);
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
        System.out.print("Selecione a conta (0-" + contas.size() + "): ");
        
        int escolha = sc.nextInt();
        sc.nextLine(); 

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
            ConsolaUi.titulo("WebBanking - Operações");
            
            System.out.println("Conta : " + conta.getIban());
            System.out.println("Tipo  : " + conta.getTipoConta());
            System.out.println("Saldo : " + String.format("%.2f EUR", conta.getSaldo()));
            
            ConsolaUi.secao("Menu de Operações");
            System.out.println("1 - Consultar Saldo");
            System.out.println("2 - Fazer Transferências");
            System.out.println("3 - Ver Movimentos");
            System.out.println("0 - Sair da Conta");
            
            ConsolaUi.linha();
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();
            sc.nextLine(); 

            switch (opcao) {
                case 1:
                    CanalAcesso.consultarSaldo(sc, conta);
                    break;
                case 2:
                    // usa o método centralizado em CanalAcesso
                    CanalAcesso.fazerTransferencia(sc, conta, contaRepo);
                    break;
                case 3:
                    verMovimentos(sc, conta);
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

    private static void verMovimentos(Scanner sc, Conta conta) {
        Utils.limparTela();
        ConsolaUi.titulo("Histórico de Movimentos");
        
        System.out.println("Conta: " + conta.getIban());
        ConsolaUi.linha();

        if (conta.getMovimentos() == null || conta.getMovimentos().isEmpty()) {
            Utils.aviso("Nenhum movimento registado nesta conta.");
        } else {
            ConsolaUi.secao("Movimentos Recentes (Mais Recente Primeiro)");
            
            for (int i = conta.getMovimentos().size() - 1; i >= 0; i--) {
                System.out.println(conta.getMovimentos().get(i));
                ConsolaUi.linha();
            }
            
            System.out.println("Total de movimentos: " + conta.getMovimentos().size());
        }

        ConsolaUi.pausa(sc);
    }

    private static void alterarPassword(Scanner sc, Cliente cliente) {
        Utils.limparTela();
        ConsolaUi.titulo("Alterar Password");
        
        ClienteCSVRepository clienteRepo = new ClienteCSVRepository();
        
        System.out.print("Password atual: ");
        String passwordAtual = sc.nextLine().trim();
        
        if (!passwordAtual.equals(cliente.getSenha())) {
            Utils.erro("Password atual incorreta!");
            ConsolaUi.pausa(sc);
            return;
        }
        
        System.out.print("Nova password: ");
        String novaPassword = sc.nextLine().trim();
        
        if (novaPassword.length() < 4) {
            Utils.erro("A password deve ter pelo menos 4 caracteres.");
            ConsolaUi.pausa(sc);
            return;
        }
        
        System.out.print("Confirme a nova password: ");
        String confirmacao = sc.nextLine().trim();
        
        if (!novaPassword.equals(confirmacao)) {
            Utils.erro("As passwords não coincidem!");
            ConsolaUi.pausa(sc);
            return;
        }
        
        cliente.setSenha(novaPassword);
        clienteRepo.atualizar(cliente);
        
        Utils.sucesso("Password alterada com sucesso!");
        ConsolaUi.pausa(sc);
    }
}