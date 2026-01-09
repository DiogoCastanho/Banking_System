package src.menus;

import java.util.*;
import src.models.Cliente;
import src.models.Conta;
import src.repository.*;
import src.ui.ConsolaUi;
import src.utils.*;

public class menuWebBanking {

    public static void showMenuWebBanking() {
        Scanner sc = new Scanner(System.in);
        ClienteCSVRepository clienteRepo = new ClienteCSVRepository();
        ContaCSVRepository contaRepo = new ContaCSVRepository();

        // Login do WebBanking
        Cliente clienteLogado = loginWebBanking(sc, clienteRepo);
        
        if (clienteLogado == null) {
            return; // Volta ao menu principal se login falhar
        }

        // Menu principal do WebBanking
        menuPrincipalWebBanking(sc, clienteLogado, contaRepo);
    }

    // ========== LOGIN ==========
    private static Cliente loginWebBanking(Scanner sc, ClienteCSVRepository clienteRepo) {
        int tentativas = 0;
        final int MAX_TENTATIVAS = 3;

        while (tentativas < MAX_TENTATIVAS) {
            Utils.limparTela();
            ConsolaUi.titulo("WebBanking - Login");
            
            System.out.print("Utilizador: ");
            String utilizador = sc.nextLine().trim();
            
            System.out.print("Palavra-passe: ");
            String senha = sc.nextLine().trim();

            Cliente cliente = clienteRepo.buscarInfoCliente(utilizador, senha);
            
            if (cliente != null) {
                Utils.sucesso("Login efetuado com sucesso! Bem-vindo, " + cliente.getNome());
                Session.setCurrentCliente(cliente);
                ConsolaUi.pausa(sc);
                return cliente;
            }

            tentativas++;
            Utils.erro("Credenciais inválidas! Tentativa " + tentativas + "/" + MAX_TENTATIVAS);
            
            if (tentativas >= MAX_TENTATIVAS) {
                Utils.erro("Número máximo de tentativas excedido. Acesso bloqueado.");
                ConsolaUi.pausa(sc);
                return null;
            }
            
            ConsolaUi.pausa(sc);
        }
        
        return null;
    }

    // ========== MENU PRINCIPAL ==========
    private static void menuPrincipalWebBanking(Scanner sc, Cliente cliente, ContaCSVRepository contaRepo) {
        int opcao;

        do {
            Utils.limparTela();
            ConsolaUi.titulo("WebBanking - " + cliente.getNome());
            
            System.out.println("1 - Ver Minhas Contas");
            System.out.println("2 - Operar numa Conta");
            System.out.println("3 - Alterar Palavra-passe");
            System.out.println("0 - Logout");
            
            ConsolaUi.linha();
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();
            sc.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    verMinhasContas(sc, cliente, contaRepo);
                    break;
                case 2:
                    operarConta(sc, cliente, contaRepo);
                    break;
                case 3:
                    alterarSenha(sc, cliente);
                    break;
                case 0:
                    Utils.sucesso("Logout efetuado. Até breve!");
                    Session.clearCliente();
                    ConsolaUi.pausa(sc);
                    break;
                default:
                    Utils.erro("Opção inválida!");
                    ConsolaUi.pausa(sc);
            }

        } while (opcao != 0);
    }

    // ========== VER MINHAS CONTAS ==========
    private static void verMinhasContas(Scanner sc, Cliente cliente, ContaCSVRepository contaRepo) {
        Utils.limparTela();
        ConsolaUi.titulo("Minhas Contas");

        List<Conta> contas = contaRepo.buscarContasCliente(cliente.getNif());

        if (contas.isEmpty()) {
            Utils.aviso("Não possui contas associadas.");
        } else {
            ConsolaUi.secao("Contas de " + cliente.getNome());
            
            for (int i = 0; i < contas.size(); i++) {
                Conta c = contas.get(i);
                System.out.println("\n[" + (i + 1) + "] " + c.getTipoConta());
                System.out.println("    IBAN  : " + c.getIban());
                System.out.println("    Saldo : " + String.format("%.2f EUR", c.getSaldo()));
                ConsolaUi.linha();
            }
            
            System.out.println("Total de contas: " + contas.size());
        }

        ConsolaUi.pausa(sc);
    }

    // ========== OPERAR CONTA ==========
    private static void operarConta(Scanner sc, Cliente cliente, ContaCSVRepository contaRepo) {
        List<Conta> contas = contaRepo.buscarContasCliente(cliente.getNif());

        if (contas.isEmpty()) {
            Utils.aviso("Não possui contas para operar.");
            ConsolaUi.pausa(sc);
            return;
        }

        // Selecionar conta
        Utils.limparTela();
        ConsolaUi.titulo("Selecionar Conta");
        
        for (int i = 0; i < contas.size(); i++) {
            Conta c = contas.get(i);
            System.out.println("[" + (i + 1) + "] " + c.getTipoConta() + " - " + c.getIban() + 
                             " (Saldo: " + String.format("%.2f EUR", c.getSaldo()) + ")");
        }
        
        ConsolaUi.linha();
        System.out.print("Escolha a conta (1-" + contas.size() + "): ");
        int escolha = sc.nextInt();
        sc.nextLine();

        if (escolha < 1 || escolha > contas.size()) {
            Utils.erro("Opção inválida!");
            ConsolaUi.pausa(sc);
            return;
        }

        Conta contaSelecionada = contas.get(escolha - 1);
        Session.setCurrentConta(contaSelecionada);
        
        // Menu de operações da conta
        menuOperacoesConta(sc, contaSelecionada, contaRepo);
    }

    // ========== MENU OPERAÇÕES CONTA ==========
    private static void menuOperacoesConta(Scanner sc, Conta conta, ContaCSVRepository contaRepo) {
        int opcao;

        do {
            Utils.limparTela();
            ConsolaUi.titulo("WebBanking - Operações");
            
            System.out.println("Conta: " + conta.getIban());
            System.out.println("Tipo : " + conta.getTipoConta());
            System.out.println("Saldo: " + String.format("%.2f EUR", conta.getSaldo()));
            
            ConsolaUi.secao("Operações Disponíveis");
            System.out.println("1 - Consultar Saldo Detalhado");
            System.out.println("2 - Depositar Dinheiro");
            System.out.println("3 - Transferir Dinheiro");
            System.out.println("4 - Ver Movimentos");
            System.out.println("0 - Voltar");
            
            ConsolaUi.linha();
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    consultarSaldoDetalhado(sc, conta);
                    break;
                case 2:
                    depositarDinheiro(sc, conta, contaRepo);
                    break;
                case 3:
                    transferirDinheiro(sc, conta, contaRepo);
                    break;
                case 4:
                    verMovimentos(sc, conta);
                    break;
                case 0:
                    Session.clearConta();
                    break;
                default:
                    Utils.erro("Opção inválida!");
                    ConsolaUi.pausa(sc);
            }

        } while (opcao != 0);
    }

    // ========== CONSULTAR SALDO ==========
    private static void consultarSaldoDetalhado(Scanner sc, Conta conta) {
        Utils.limparTela();
        ConsolaUi.titulo("Consultar Saldo");
        
        System.out.println("IBAN         : " + conta.getIban());
        System.out.println("Tipo de Conta: " + conta.getTipoConta());
        System.out.println("NIF Titular  : " + conta.getNifCliente());
        ConsolaUi.linha();
        System.out.println("SALDO ATUAL  : " + String.format("%.2f EUR", conta.getSaldo()));
        ConsolaUi.linha();
        
        Utils.sucesso("Consulta realizada com sucesso.");
        ConsolaUi.pausa(sc);
    }

    // ========== DEPOSITAR DINHEIRO ==========
    private static void depositarDinheiro(Scanner sc, Conta conta, ContaCSVRepository contaRepo) {
        Utils.limparTela();
        ConsolaUi.titulo("Depositar Dinheiro");
        
        System.out.println("Saldo atual: " + String.format("%.2f EUR", conta.getSaldo()));
        ConsolaUi.linha();
        
        System.out.print("Valor a depositar (EUR): ");
        double valor = sc.nextDouble();
        sc.nextLine();

        if (valor <= 0) {
            Utils.erro("Valor inválido para depósito.");
            ConsolaUi.pausa(sc);
            return;
        }

        // Atualizar saldo
        double saldoAntigo = conta.getSaldo();
        conta.depositarDinheiro(valor);
        
        // Persistir alteração
        contaRepo.atualizar(conta);
        
        Utils.sucesso("Depósito de " + String.format("%.2f EUR", valor) + " realizado com sucesso!");
        System.out.println("Saldo anterior: " + String.format("%.2f EUR", saldoAntigo));
        System.out.println("Saldo atual   : " + String.format("%.2f EUR", conta.getSaldo()));
        
        ConsolaUi.pausa(sc);
    }

    // ========== TRANSFERIR DINHEIRO ==========
    private static void transferirDinheiro(Scanner sc, Conta conta, ContaCSVRepository contaRepo) {
        Utils.limparTela();
        ConsolaUi.titulo("Transferir Dinheiro");
        
        System.out.println("Saldo disponível: " + String.format("%.2f EUR", conta.getSaldo()));
        ConsolaUi.linha();
        
        System.out.print("IBAN do destinatário: ");
        String ibanDestinatario = sc.nextLine().trim();
        
        System.out.print("Valor a transferir (EUR): ");
        double valor = sc.nextDouble();
        sc.nextLine();

        if (valor <= 0) {
            Utils.erro("Valor inválido para transferência.");
            ConsolaUi.pausa(sc);
            return;
        }

        if (valor > conta.getSaldo()) {
            Utils.erro("Saldo insuficiente para a transferência.");
            ConsolaUi.pausa(sc);
            return;
        }

        // Buscar conta destinatário
        Conta contaDestinatario = contaRepo.buscarPorIban(ibanDestinatario);
        
        if (contaDestinatario == null) {
            Utils.erro("IBAN do destinatário não encontrado.");
            ConsolaUi.pausa(sc);
            return;
        }

        if (conta.getIban().equals(ibanDestinatario)) {
            Utils.erro("Não pode transferir para a mesma conta.");
            ConsolaUi.pausa(sc);
            return;
        }

        // Confirmar transferência
        ConsolaUi.linha();
        System.out.println("Confirmar transferência:");
        System.out.println("De     : " + conta.getIban());
        System.out.println("Para   : " + ibanDestinatario);
        System.out.println("Valor  : " + String.format("%.2f EUR", valor));
        ConsolaUi.linha();
        System.out.print("Confirma? (S/N): ");
        String confirmacao = sc.nextLine().trim();

        if (!confirmacao.equalsIgnoreCase("S")) {
            Utils.aviso("Transferência cancelada.");
            ConsolaUi.pausa(sc);
            return;
        }

        // Realizar transferência
        conta.transferirDinheiro(ibanDestinatario, valor);
        contaDestinatario.receberTransferencia(conta.getIban(), valor);
        
        // Persistir alterações
        contaRepo.atualizar(conta);
        contaRepo.atualizar(contaDestinatario);
        
        Utils.sucesso("Transferência de " + String.format("%.2f EUR", valor) + " realizada com sucesso!");
        System.out.println("Novo saldo: " + String.format("%.2f EUR", conta.getSaldo()));
        
        ConsolaUi.pausa(sc);
    }

    // ========== VER MOVIMENTOS ==========
    private static void verMovimentos(Scanner sc, Conta conta) {
        Utils.limparTela();
        ConsolaUi.titulo("Histórico de Movimentos");
        
        System.out.println("Conta: " + conta.getIban());
        ConsolaUi.linha();

        if (conta.getMovimentos() == null || conta.getMovimentos().isEmpty()) {
            Utils.aviso("Nenhum movimento registado nesta conta.");
        } else {
            ConsolaUi.secao("Últimos Movimentos");
            
            for (int i = conta.getMovimentos().size() - 1; i >= 0; i--) {
                System.out.println(conta.getMovimentos().get(i));
                ConsolaUi.linha();
            }
            
            System.out.println("Total de movimentos: " + conta.getMovimentos().size());
        }

        ConsolaUi.pausa(sc);
    }

    // ========== ALTERAR SENHA ==========
    private static void alterarSenha(Scanner sc, Cliente cliente) {
        Utils.limparTela();
        ConsolaUi.titulo("Alterar Palavra-passe");
        
        ClienteCSVRepository clienteRepo = new ClienteCSVRepository();
        
        System.out.print("Palavra-passe atual: ");
        String senhaAtual = sc.nextLine().trim();
        
        if (!senhaAtual.equals(cliente.getSenha())) {
            Utils.erro("Palavra-passe atual incorreta!");
            ConsolaUi.pausa(sc);
            return;
        }
        
        System.out.print("Nova palavra-passe: ");
        String novaSenha = sc.nextLine().trim();
        
        if (novaSenha.length() < 4) {
            Utils.erro("A palavra-passe deve ter pelo menos 4 caracteres.");
            ConsolaUi.pausa(sc);
            return;
        }
        
        System.out.print("Confirme a nova palavra-passe: ");
        String confirmacao = sc.nextLine().trim();
        
        if (!novaSenha.equals(confirmacao)) {
            Utils.erro("As palavras-passe não coincidem!");
            ConsolaUi.pausa(sc);
            return;
        }
        
        // Atualizar senha
        cliente.setSenha(novaSenha);
        clienteRepo.atualizar(cliente);
        
        Utils.sucesso("Palavra-passe alterada com sucesso!");
        ConsolaUi.pausa(sc);
    }
}