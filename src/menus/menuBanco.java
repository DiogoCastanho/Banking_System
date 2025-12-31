package src.menus;

import src.models.Banco;
import src.models.Cliente;
import src.repository.*;

import java.util.*;
import src.utils.*;

public class menuBanco {

    public static void showMenuBanco() {
        Banco banco = new Banco("Banco do mell");
        Scanner sc = new Scanner(System.in);

        if (!loginAdmin(sc)) {
            System.out.println("Login falhou. A sair...");
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
                    ClienteService clienteService = new ClienteService();

                    System.out.println("\n== Criar Cliente");

                    String nome = Utils.lerTextoObrigatorio(sc, "Nome: ");
                    String nif = Utils.lerTextoObrigatorio(sc, "NIF: ");
                    String utilizador = Utils.lerTextoObrigatorio(sc, "Utilizador: ");
                    String senha = Utils.lerSenha(sc, "Palavra-passe: ");

                    try {
                        Cliente cliente = clienteService.criarCliente(
                                nome, nif, utilizador, senha
                        );

                        System.out.println("\nConta Associada:\n" + cliente.getConta());
                        Utils.sucesso("Cliente criado com sucesso!");

                    } catch (IllegalArgumentException e) {
                        Utils.erro(e.getMessage());
                    }
                    break;
                case 2:
                    // ClienteCSVRepository clienteRepository = new ClienteCSVRepository();
                    // List<Cliente> clientes = clienteRepository.listarClientes("clientes.csv");
                    // System.out.println("\n== Listar Clientes");
                    // for (Cliente c : clientes) {
                    //     System.out.println(c);
                    // }
                    
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

        System.out.println("\n=== Login ===");
        System.out.print("Utilizador: ");
        String user = sc.nextLine();
        System.out.print("Palavra-passe: ");
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
