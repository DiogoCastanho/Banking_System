package src.models;

import java.util.Scanner;
import src.repository.ContaCSVRepository;
import src.ui.ConsolaUi;
import src.utils.Utils;
import src.repository.MovimentoCSVRepository;

public abstract class CanalAcesso {

  public static void consultarSaldo(Scanner sc, Conta conta) {
    Utils.limparTela();
    ConsolaUi.titulo("Consultar Saldo");

    System.out.println("IBAN          : " + conta.getIban());
    System.out.println("Tipo de Conta : " + conta.getTipoConta());
    System.out.println("NIF Titular   : " + conta.getNifCliente());
    ConsolaUi.linha();
    System.out.println("SALDO ATUAL   : " + String.format("%.2f EUR", conta.getSaldo()));
    ConsolaUi.linha();

    Utils.sucesso("Consulta realizada com sucesso.");
    ConsolaUi.pausa(sc);
  }

  public static void fazerTransferencia(Scanner sc, Conta conta, ContaCSVRepository contaRepo) {
    Utils.limparTela();
    ConsolaUi.titulo("Transferir Dinheiro");

    System.out.println("Saldo disponível: " + String.format("%.2f EUR", conta.getSaldo()));
    ConsolaUi.linha();

    System.out.print("IBAN do destinatário: ");
    String ibanDestinatario = sc.nextLine().trim();

    if (ibanDestinatario.isEmpty()) {
      Utils.aviso("Operação cancelada.");
      ConsolaUi.pausa(sc);
      return;
    }

    System.out.print("Valor a transferir (EUR): ");
    double valor;
    try {
      valor = Double.parseDouble(sc.nextLine().trim());
    } catch (NumberFormatException e) {
      Utils.erro("Valor inválido para transferência.");
      ConsolaUi.pausa(sc);
      return;
    }

    if (valor <= 0) {
      Utils.erro("Valor inválido para transferência.");
      ConsolaUi.pausa(sc);
      return;
    }

    if (valor > conta.getSaldo()) {
      Utils.erro("Saldo insuficiente para a transferência.");
      ConsolaUi.pausa(sc);
      return;
    }

    Conta contaDestinatario = contaRepo.buscarPorIban(ibanDestinatario);

    if (contaDestinatario == null) {
      Utils.erro("IBAN do destinatário não encontrado.");
      ConsolaUi.pausa(sc);
      return;
    }

    if (conta.getIban().equals(ibanDestinatario)) {
      Utils.erro("Não pode transferir para a mesma conta.");
      ConsolaUi.pausa(sc);
      return;
    }

    ConsolaUi.linha();
    System.out.println("Confirmar Transferência:");
    System.out.println("De    : " + conta.getIban());
    System.out.println("Para  : " + ibanDestinatario);
    System.out.println("Valor : " + String.format("%.2f EUR", valor));
    ConsolaUi.linha();
    System.out.print("Confirma a transferência? (S/N): ");
    String confirmacao = sc.nextLine().trim();

    if (!confirmacao.equalsIgnoreCase("S")) {
      Utils.aviso("Transferência cancelada.");
      ConsolaUi.pausa(sc);
      return;
    }

    double saldoAnterior = conta.getSaldo();

    // Atualiza saldos diretamente (CanalAcesso está no mesmo package, pode aceder a campos protegidos)
    conta.saldo -= valor;
    contaDestinatario.saldo += valor;

    // Regista movimentos diretamente no CSV de movimentos
    MovimentoCSVRepository movimentoRepo = new MovimentoCSVRepository();

    Movimento movEnvio = new Movimento(
        new java.util.Date(),
        valor,
        conta.getSaldo(),
        TipoMove.enviar,
        ibanDestinatario
    );
    conta.getMovimentos().add(movEnvio);
    movimentoRepo.salvar(conta.getIban(), movEnvio);

    Movimento movReceber = new Movimento(
        new java.util.Date(),
        valor,
        contaDestinatario.getSaldo(),
        TipoMove.receber,
        conta.getIban()
    );
    contaDestinatario.getMovimentos().add(movReceber);
    movimentoRepo.salvar(contaDestinatario.getIban(), movReceber);

    // Atualiza ficheiro de contas
    contaRepo.atualizar(conta);
    contaRepo.atualizar(contaDestinatario);

    Utils.sucesso("Transferência de " + String.format("%.2f EUR", valor) + " realizada com sucesso!");
    System.out.println("Saldo anterior : " + String.format("%.2f EUR", saldoAnterior));
    System.out.println("Saldo atual    : " + String.format("%.2f EUR", conta.getSaldo()));

    ConsolaUi.pausa(sc);
  }

}