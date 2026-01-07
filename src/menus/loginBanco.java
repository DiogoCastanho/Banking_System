package src.menus;

import java.util.Scanner;
import src.repository.ClienteCSVRepository;
import src.models.Cliente;
import src.utils.Utils;
import src.ui.ConsolaUi;

public class loginBanco {

    public static void showLogin() {
        Scanner sc = new Scanner(System.in);
        ClienteCSVRepository clienteRepo = new ClienteCSVRepository();

        while (true) {
            Utils.limparTela();
            ConsolaUi.titulo("Login Banco");

            System.out.print("Utilizador: ");
            String utilizador = sc.nextLine().trim();

            System.out.print("Palavra-passe: ");
            String senha = sc.nextLine().trim();

            if (utilizador.equals("admin") && senha.equals("admin")) {
                Utils.sucesso("Login admin efetuado.");
                ConsolaUi.pausa(sc);
                menuBanco.showMenuBanco();
                return;
            }

            Cliente cliente = clienteRepo.buscarInfoCliente(utilizador, senha);
            if (cliente != null) {
                Utils.sucesso("Login cliente efetuado: " + cliente.getUtilizador());
                ConsolaUi.pausa(sc);
                menuBancoCliente.showMenuBancoCliente(cliente);
                return;
            }

            Utils.erro("Credenciais inválidas. Tente novamente.");
            ConsolaUi.pausa(sc);
        }
    }
}