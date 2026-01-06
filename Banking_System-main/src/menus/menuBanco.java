package src.menus;

import java.util.*;
import src.Banco;
import src.Cliente;
import src.Conta;
import src.utils.Utils;

public class menuBanco {

    public static void showMenuBanco() {
        // Criar banco
        Banco banco = new Banco("Banco do mell");

        Scanner sc = new Scanner(System.in);

        if (!loginAdmin(sc)) {
            System.out.println("Login falhou. Tente novamente");
            return;
        }

        int opcao;
        do {
            Utils.limparTela();
            System.out.println("\n--- Menu Banco ---");

            System.out.println("\n=== Gestão de Clientes ===");
            System.out.println("1 - Criar Cliente");
            System.out.println("2 - Listar Clientes");
            System.out.println("3 - Editar Cliente");
            System.out.println("4 - Remover Cliente");

            System.out.println("\n=== Gestão de Contas ===");
            System.out.println("5 - Criar Conta");
            System.out.println("6 - Listar Contas");
            System.out.println("7 - Remover Cliente");

            System.out.println("\n=== Cartões e Acessos ===");
            System.out.println("8 - Desbloquear Cartão (ATM)");
            System.out.println("0 - Logout");
            System.out.print("Opção: ");

            opcao = sc.nextInt();
            sc.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    System.out.println("\n== Criar Cliente");
                    String nome = Utils.lerNome(sc);
                    String nif = Utils.lerNif(sc);
                    String utilizador = Utils.lerUtiizador(sc);
                    String senha = Utils.lerPalavraP(sc);

                    Conta newConta = new Conta(new Cartao(1, LocalDate(2025, 01, 12), 123, 1123), null, 0, null);
                    Cliente newcliente = new Cliente(nome, nif, utilizador, senha, newConta);
                    banco.criarCliente(newcliente);
                    Utils.sucesso("Cliente criado com sucesso");
                    break;
                case 2:
                    System.out.println("\n== Listar Clientes");
                    banco.listarClientes();
                    break;
                case 3:
                    System.out.println("\n== Editar Cliente");
                    break;
                case 4:
                    System.out.println("\n== Remover Cliente");
                    break;
                case 5:
                    System.out.println("\n== Criar Conta");
                    break;
                case 6:
                    System.out.println("\n== Listar Contas");
                    break;
                case 7:
                    System.out.println("\n== Remover Conta");
                    break;
                case 8:
                    System.out.println("\n== Desbloquear Cartão (ATM)");
                    break;
                case 0:
                    System.out.println("A voltar ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    // Login method
    private static boolean loginAdmin(Scanner sc) {
        final String ADMIN_USER = "admin";
        final String ADMIN_PASS = "admin";

        System.out.println("\n=== Login Admin ===");
        System.out.print("User: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
            Utils.sucesso("Login bem-sucedido!");
            return true;
        } else {
            Utils.erro("User ou password incorretos!");
            return false;
        }
    }
}
