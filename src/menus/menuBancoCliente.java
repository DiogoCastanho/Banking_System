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

            System.out.println("1 - Aceder Conta");
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
                        Utils.aviso("Não possui contas associadas ao seu perfil.");
                    }
                    ConsolaUi.pausa(sc);
                    break;
                case 2:
                    System.out.println("Movimentos:");
                    if (cliente.getConta() != null && cliente.getConta().getMovimentos() != null) {
                        cliente.getConta().getMovimentos().forEach(m -> System.out.println(m));
                    } else {
                        Utils.aviso("Sem movimentos para mostrar.");
                    }
                    ConsolaUi.pausa(sc);
                    break;
                case 0:
                    Utils.sucesso("Logout efetuado. Até breve!");
                    ConsolaUi.pausa(sc);
                    break;
                default:
                    Utils.erro("Opção inválida!");
                    ConsolaUi.pausa(sc);
            }
        } while (opcao != 0);
    }
}
