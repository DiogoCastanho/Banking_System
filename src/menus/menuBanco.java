package src.menus;

import java.util.*;

import src.Banco;
import src.Cliente;
import src.Conta;
import src.utils.Utils;

public class menuBanco {

  public static void showMenuBanco() {
    Banco banco = new Banco("Banco do mell");
    // Cliente cliente1 = new Cliente("Diogo","26012123", "Brownie", "Senha", new Conta(null, null, 0, null));
    // Cliente cliente2 = new Cliente("João","26012123", "Brownie", "Senha", new Conta(null, null, 0, null));
    // Cliente cliente3 = new Cliente("Marta","26012123", "Brownie", "Senha", new Conta(null, null, 0, null));
    // banco.criarCliente(cliente3);
    // banco.criarCliente(cliente1);
    // banco.criarCliente(cliente2);

    Scanner sc = new Scanner(System.in);
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

          switch (opcao) {
              case 1:
                // create client section
                System.out.println("\n== Criar Cliente");

                String nome = Utils.lerNome(sc);
                String nif = Utils.lerNif(sc);
                String utilizador = Utils.lerUtiizador(sc);
                String senha = Utils.lerPalavraP(sc);

                // Create account section
                Conta newConta = new Conta(null, null, 0, null);
                // create new client
                Cliente newcliente = new Cliente(nome, nif, utilizador, senha, newConta);
                System.out.println("\nPrima ENTER para continuar...");
                sc.nextLine();
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
                System.out.println("\n== Desbloquear Cartão (Atm)");
                break;
              case 0:
                System.out.println("A voltar ao menu principal...");
                break;
              default:
                System.out.println("Opção inválida!");
          }
          
        } while(opcao != 0);
  }
  
}
