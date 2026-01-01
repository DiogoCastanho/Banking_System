package src.ui;

import java.util.Scanner;

public class ConsolaUi {

  public static void titulo(String texto) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("  " + texto.toUpperCase());
        System.out.println("========================================");
    }

    public static void secao(String texto) {
        System.out.println("\n--- " + texto + " ---");
    }

    public static void linha() {
        System.out.println("----------------------------------------");
    }

    public static void pausa(Scanner sc) {
      System.out.println("\nPrima ENTER para continuar...");
      sc.nextLine();
    }


}
