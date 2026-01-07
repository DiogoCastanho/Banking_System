package src.menus;

import java.util.Scanner;
import src.models.Cliente;
import src.ui.ConsolaUi;
import src.utils.Utils;

public class menuBancoCliente {

    public static void showMenuBancoCliente(Cliente cliente) {
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            Utils.limparTela();
            ConsolaUi.titulo("Menu Cliente - " + cliente.getNome());

            System.out.println("1 - Consultar Conta");
            System.out.println("2 - Consultar Movimento(s)");
            System.out.println("0 - Voltar");

            ConsolaUi.linha();
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("Conta do cliente: ");
                    if (cliente.getConta() != null) {
                        System.out.println(cliente.getConta().toString());
                    } else {
                        Utils.erro("Cliente sem conta associada.");
                    }
                    ConsolaUi.pausa(sc);
                    break;
                case 2:
                    System.out.println("Movimentos:");
                    if (cliente.getConta() != null && cliente.getConta().getMovimentos() != null) {
                        cliente.getConta().getMovimentos().forEach(m -> System.out.println(m));
                    } else {
                        Utils.erro("Sem movimentos para mostrar.");
                    }
                    ConsolaUi.pausa(sc);
                    break;
                case 0:
                    return;
                default:
                    Utils.erro("Opção inválida!");
            }
        } while (opcao != 0);
    }
}
