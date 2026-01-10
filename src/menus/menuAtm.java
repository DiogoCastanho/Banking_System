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
    ContaCSVRepository contaRepo = new ContaCSVRepository();
    // ATM atm = new ATM("ATM do " + banco);
    Scanner sc = new Scanner(System.in);

    if (!login(sc)) {
          return;
    }

    int opcao;

    do {
        Utils.limparTela();
        ConsolaUi.titulo("Menu ATM "+ banco.getNome());
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
            Utils.limparTela();
            ConsolaUi.titulo("Consultar Saldo");
            System.out.println("Saldo Atual: " + src.utils.Session.getCurrentConta().getSaldo());
            contaRepo.atualizar(src.utils.Session.getCurrentConta());
            ConsolaUi.pausa(sc);
            break;
          case 2:
            Utils.limparTela();
            ConsolaUi.titulo("Levantar Dinheiro");
            System.out.println("Saldo Atual: " + src.utils.Session.getCurrentConta().getSaldo());
            System.out.println("Quantidade a levantar: ");
            double quantidade = sc.nextDouble();
            src.utils.Session.getCurrentConta().levantarDinheiro(quantidade);
            contaRepo.atualizar(src.utils.Session.getCurrentConta());
            break;
          case 3:
            Utils.limparTela();
            ConsolaUi.titulo("Transferir Dinheiro");
            System.out.println("Saldo Atual: " + src.utils.Session.getCurrentConta().getSaldo());
            System.out.println("Iban do destinatário: ");
            String ibanDestinatario = sc.nextLine();
            System.out.println("Quantidade a transferir: ");
            double quantia = sc.nextDouble();
            src.utils.Session.getCurrentConta().transferirDinheiro(ibanDestinatario,  quantia);

            Conta contaDestinatario = contaRepo.buscarPorIban(ibanDestinatario);
            contaDestinatario.receberTransferencia(ibanDestinatario, quantia);
            
            contaRepo.atualizar(src.utils.Session.getCurrentConta());
            contaRepo.atualizar(contaDestinatario);
            ConsolaUi.pausa(sc);
            break;
          case 4:
            Utils.limparTela();
            ConsolaUi.titulo("Últimos Movimentos");
            if(src.utils.Session.getCurrentConta().getMovimentos().isEmpty()) {
                System.out.println("Nenhum movimento encontrado.");
            } else {
                    System.out.println(src.utils.Session.getCurrentConta().getMovimentos());
            }
            ConsolaUi.pausa(sc);
            break;
          case 5:
            Utils.limparTela();
            ConsolaUi.titulo("Alterar Pin");
            System.out.println("Novo Pin: ");
            int pin = sc.nextInt();
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
    ContaCSVRepository contaRepo = new ContaCSVRepository(); // Instanciar fora do loop

    do {
        ConsolaUi.titulo("Login");
        System.out.print("Nº Cartão: ");
        String n_cartao = sc.nextLine();

        // 1. Verificar primeiro se a conta existe e se já está bloqueada
        Conta contaExistente = contaRepo.buscarCartaoPorNumero(n_cartao);
        if (contaExistente != null && contaExistente.getCartao().isBloqueado()) {
            Utils.erro("Este cartão encontra-se bloqueado. Contacte o banco.");
            ConsolaUi.pausa(sc);
            return false;
        }
        else if(contaExistente == null) {
            System.out.println("Cartão não encontrado.");
            ConsolaUi.pausa(sc);
            return false;
        }

        System.out.print("Pin: ");
        String pinStr = sc.nextLine();
        int pin = Integer.parseInt(pinStr);

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
