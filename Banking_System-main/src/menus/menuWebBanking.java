package src.menus;

import java.util.*;

import src.utils.Utils;

public class menuWebBanking {
//teste
  public static void showMenuWebBanking() {
    Scanner sc = new Scanner(System.in);
    int opcao;

      do {
          Utils.limparTela();
          System.out.println("\n--- Menu WebBanking ---");
          System.out.println("1 - Levantar dinheiro");
          System.out.println("2 - Consultar saldo");
          System.out.println("3 - Consultar movimentos");
          System.out.println("4 - Criar cliente");
          System.out.println("0 - Voltar");
          System.out.print("Opção: ");

          opcao = sc.nextInt();

          switch (opcao) {
              case 1:
                System.out.println("Levantar dinheiro...");
                break;
              case 2:
                System.out.println("Consultar saldo...");
                break;
              case 3:
                System.out.println("Consultar movimentos...");
                break;
              case 4:
                System.out.println("Criar cliente...");
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
