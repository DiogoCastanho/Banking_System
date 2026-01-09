package src.ui;

import java.util.List;
import java.util.Scanner;
import src.utils.*;

import src.models.Conta;

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

    public static void mostrarContas(List<Conta> contas, String nif, Scanner sc) {

    ConsolaUi.linha();
    System.out.println("Contas associadas ao cliente (NIF: " + nif + ")");
    ConsolaUi.linha();
    System.out.println();

    int index = 1;
    for (Conta conta : contas) {

        System.out.println("[" + index + "] Nº Conta : " + conta.getIban());
        System.out.println("    Saldo    : " + conta.getSaldo() + " EUR");
        System.out.println("    Tipo     : " + conta.getTipoConta());
        ConsolaUi.linha();

        index++;
    }

    System.out.println("Total de contas: " + contas.size());
    ConsolaUi.pausa(sc);
}



}
