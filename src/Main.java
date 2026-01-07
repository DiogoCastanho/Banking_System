package src;
import java.util.*;

import src.menus.menuAtm;
import src.menus.loginBanco;
import src.menus.menuWebBanking;
import src.ui.ConsolaUi;
import src.utils.*;

public class Main {
  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    int opcao;

    do {
      ConsolaUi.titulo("Sistema Bancário");
      System.out.println("1 - Banco");
      System.out.println("2 - Atm");
      System.out.println("3 - WebBanking");
      System.out.println("0 - Sair");

      ConsolaUi.linha();
      System.out.print("Escolha uma opção: ");

      opcao = sc.nextInt();

      switch (opcao) {
        case 1:
          loginBanco.showLogin();
          break;
        case 2:
          menuAtm.showMenuAtm();
          break;
        case 3:
          menuWebBanking.showMenuWebBanking();
          break;
        case 0:
          System.err.println("A sair...");
          break;
        default:
          Utils.erro("Opção inválida!");
          break;
      }

    } while (opcao != 0);
  } 
  
}
