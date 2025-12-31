package src.menus;
import java.util.*;


public class menuAtm {

  public static void showMenuAtm() {
    Scanner sc = new Scanner(System.in);
    int opcao;

      do {
          System.out.println("\n=== Menu Atm ===");
          System.out.println("1 - Levantar dinheiro");
          System.out.println("2 - Depositar dinheiro");
          System.out.println("0 - Voltar");
          System.out.print("Opção: ");

          opcao = sc.nextInt();

          switch (opcao) {
              case 1:
                System.out.println("Levantar dinheiro...");
                break;
              case 2:
                System.out.println("Depositar dinheiro...");
                break;
              case 0:
                System.out.println("A voltar...");
                break;
              default:
                System.out.println("Opção inválida!");
          }
          
        } while(opcao != 0);
  }
  
}
