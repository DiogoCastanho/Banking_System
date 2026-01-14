package src.menus;

import src.models.CanalAcesso;
import src.models.Conta;
import src.ui.ConsolaUi;
import src.repository.*;

import java.util.*;
import src.utils.*;

public class menuAtm {

  public static void showMenuAtm() {
    ContaCSVRepository contaRepo = new ContaCSVRepository();
    // ATM atm = new ATM("ATM do " + banco);
    Scanner sc = new Scanner(System.in);

    if (!login(sc)) {
          return;
    }

    int opcao;

    do {
        Utils.limparTela();
        ConsolaUi.titulo("Menu ATM ");
        System.out.println("1 - Consultar Saldo");
        System.out.println("2 - Levantar dinheiro");
        System.out.println("3 - Fazer Transferências");
        System.out.println("4 - Últimos Movimentos");
        System.out.println("5 - Alterar Pin");
        System.out.println("0 - Voltar");
        ConsolaUi.linha();

        opcao = Utils.lerInteiroSeguro(sc, "Escolha uma opção: ");

        switch (opcao) {
          case 1:
            CanalAcesso.consultarSaldo(sc, src.utils.Session.getCurrentConta());
            break;
          case 2:
            Utils.limparTela();
            ConsolaUi.titulo("Levantar Dinheiro");
            double quantidade = Utils.lerDoubleSeguro(sc, "Quantidade a levantar: ");
            src.utils.Session.getCurrentConta().levantarDinheiro(quantidade);
            contaRepo.atualizar(src.utils.Session.getCurrentConta());
            break;
          case 3:
            CanalAcesso.fazerTransferencia(sc, src.utils.Session.getCurrentConta(), contaRepo);
            break;
          case 4:
            Utils.limparTela();
            ConsolaUi.titulo("Últimos Movimentos");
            if(src.utils.Session.getCurrentConta().getMovimentos().isEmpty()) {
                Utils.aviso("Nenhum movimento encontrado.");
                ConsolaUi.pausa(sc);
            } else {
                    System.out.println(src.utils.Session.getCurrentConta().getMovimentos());
            }
            break;
          case 5:
            Utils.limparTela();
            ConsolaUi.titulo("Alterar Pin");
            int pin = Utils.lerInteiroSeguro(sc, "Novo Pin: ");
            src.utils.Session.getCurrentConta().getCartao().setPin(pin);
            contaRepo.atualizar(src.utils.Session.getCurrentConta());
            ConsolaUi.pausa(sc);
            break;
          case 0:
            Utils.limparTela();
            System.out.println("A voltar...");
            src.utils.Session.clearConta();
            break;
          default:
            System.out.println("Opção inválida!");
        }
          
      } while(opcao != 0);
  }

  private static boolean login(Scanner sc) {
    int counter = 0;
    String ultimoCartao = "";
    ContaCSVRepository contaRepo = new ContaCSVRepository(); // Instanciar fora do loop

    do {
        ConsolaUi.titulo("Login");
        System.out.print("Nº Cartão: ");
        String n_cartao = sc.nextLine();

        if (!n_cartao.equals(ultimoCartao)) {
            counter = 0;
            ultimoCartao = n_cartao;
        }

        Conta contaExistente = contaRepo.buscarCartaoPorNumero(n_cartao);
        if (contaExistente != null && contaExistente.getCartao().isBloqueado()) {
            Utils.erro("Este cartão encontra-se bloqueado. Contacte o banco.");
            ConsolaUi.pausa(sc);
            return false;
        }
        else if(contaExistente == null) {
            Utils.aviso("Cartão não encontrado.");
            ConsolaUi.pausa(sc);
            return false;
        }

        int pin = Utils.lerInteiroSeguro(sc, "Pin: ");

        Conta contaLogada = contaRepo.buscarCartao(n_cartao, pin);

        if (contaLogada != null) {
            src.utils.Session.setCurrentConta(contaLogada);
            Utils.sucesso("Login bem-sucedido!");
            ConsolaUi.pausa(sc);
            return true;
        } else {
            counter++;
            Utils.erro("Nº Cartão ou pin incorretos! Tentativas: " + counter + "/3");
            
            if (counter >= 3 && contaExistente != null) {
                contaExistente.getCartao().bloquearCartao();
                // 2. IMPORTANTE: Método que grava a alteração no ficheiro contas.csv
                contaRepo.atualizar(contaExistente);
                
                Utils.erro("Limite de tentativas excedido. Cartão BLOQUEADO!");
                ConsolaUi.pausa(sc);
                return false;
            }
            ConsolaUi.pausa(sc);
        }
    } while (counter < 3);

    return false;
  }
}
