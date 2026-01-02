package src.menus;

import src.models.ATM;
import src.models.Banco;
import src.models.Conta;
import src.ui.ConsolaUi;
import src.repository.*;

import java.util.*;
import src.utils.*;

public class menuAtm {

  public static void showMenuAtm() {
    Banco banco = new Banco("Banco do Mell");
    ATM atm = new ATM("ATM do " + banco);
    Scanner sc = new Scanner(System.in);

    if (!login(sc)) {
            Utils.erro("Login falhou. A sair...");
            return;
        }
    int opcao;
    do {
        Utils.limparTela();
        ConsolaUi.titulo("Menu ATM");
        System.out.println("1 - Consultar Saldo");
        System.out.println("2 - Levantar dinheiro");
        System.out.println("3 - Transferir dinheiro");
        System.out.println("4 - Últimos Movimentos");
        System.out.println("5 - Alterar Pin");
        System.out.println("0 - Voltar");
        ConsolaUi.linha();
        System.out.print("Escolha uma opção: ");

        opcao = sc.nextInt();
        sc.nextLine(); // limpar buffer

        switch (opcao) {
          case 1:
            ContaCSVRepository contaRepository = new ContaCSVRepository();
            List<Conta> contas = contaRepository.listarContas("contas.csv");
            ConsolaUi.titulo("Saldo");
            System.out.println("Saldo: " + contas.get(0).getSaldo());
            ConsolaUi.pausa(sc);
            break;
          case 2:
            System.out.println("Levantar dinheiro...");
            break;
          case 3:
            System.out.println("Transferir dinheiro...");
            break;
          case 4:
            System.out.println("Últimos Movimentos...");
            break;
          case 5:
            System.out.println("Alterar Pin...");
            break;
          case 0:
            System.out.println("A voltar...");
            break;
          default:
            System.out.println("Opção inválida!");
        }
          
      } while(opcao != 0);
  }

  private static boolean login(Scanner sc) {

    ConsolaUi.titulo("Login");
    System.out.print("Nº Cartão: ");
    String n_cartao = sc.nextLine();
    System.out.print("Pin: ");
    String pin = sc.nextLine();

    ContaCSVRepository contaRepo = new ContaCSVRepository();
    if(contaRepo.buscarCartao(n_cartao, Integer.parseInt(pin)) != null) {
      Utils.sucesso("Login bem-sucedido!");
      ConsolaUi.pausa(sc);
        return true;
    } else {
      Utils.erro("Utilizador ou palavra-passe incorretos!");
      return false;
    }
  }
}
