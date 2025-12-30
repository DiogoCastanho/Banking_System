package src;
import java.util.*;

import src.menus.menuAtm;
import src.menus.menuBanco;
import src.menus.menuWebBanking;

public class Main {
  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    int opcao;

    do {
      System.out.println("=== Sistema Bancário ===");
      System.out.println("1 - Banco");
      System.out.println("2 - Atm");
      System.out.println("3 - WebBanking");
      System.out.println("0 - Sair");
      System.out.print("Opção: ");

      opcao = sc.nextInt();

      switch (opcao) {
        case 1:
          menuBanco.showMenuBanco();
          break;
        case 2:
          menuAtm.showMenuAtm();
          break;
        case 3:
          menuWebBanking.showMenuWebBanking();
          break;
        default:
          System.out.println("Opção inválida!");
          break;
      }

    } while (opcao != 0);
  } 
  
}
