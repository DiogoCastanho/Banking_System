package src.models;

import java.util.ArrayList;
import java.util.Scanner;

import src.repository.ContaCSVRepository;
import src.ui.ConsolaUi;
import src.utils.*;

public class Conta {
  
  protected String iban;
  protected String nifCliente;
  protected double saldo;
  protected TipoContaEnum tipoconta;
  protected ArrayList<Movimento> movimentos;
  protected Cartao cartao;

  // construtor
  public Conta(String iban, String nifCliente, double saldo, TipoContaEnum tipoconta, Cartao cartao) {
    this.iban   = iban;
    this.nifCliente = nifCliente;
    this.saldo  = saldo;
    this.tipoconta = tipoconta;
    this.cartao = cartao;
    this.movimentos = new ArrayList<>();
  }

  // getters
  public String getNifCliente() { return nifCliente; }
  public String getIban() { return iban; }
  public double getSaldo() { return saldo; }
  public TipoContaEnum getTipoConta() { return tipoconta; }
  public Cartao getCartao() { return cartao; }

  public void levantarDinheiro(double valor) {
    Scanner sc = new Scanner(System.in);

      if (valor <= 0) {
          Utils.erro("Valor inválido para levantamento.");
          return;
      }
      if (valor > saldo) {
          Utils.erro("Saldo insuficiente para o levantamento.");
          return;
      }
      saldo -= valor;
      Movimento movimento = new Movimento(new java.util.Date(), valor, saldo, TipoMove.levamentar, 0);
      movimentos.add(movimento);
      Utils.sucesso("Levantamento de " + valor + " EUR realizado com sucesso.");
      ConsolaUi.pausa(sc);

  }

  public void transferirDinheiro(String ibanDestinatario, double valor) {
      if (valor <= 0) {
          Utils.erro("Valor inválido para transferência.");
          return;
      }
      if (valor > saldo) {
          Utils.erro("Saldo insuficiente para a transferência.");
          return;
      }
      ContaCSVRepository contaRepo = new ContaCSVRepository();
      if(contaRepo.buscarPorIban(ibanDestinatario) != null) {
          Utils.erro("IBAN do destinatário inválido.");
          return;
      }
      else {
          Conta contaDestinatario = contaRepo.buscarPorIban(ibanDestinatario);
          contaDestinatario.saldo += valor;
          saldo -= valor;
          Movimento movimento = new Movimento(new java.util.Date(), valor, saldo, TipoMove.enviar, Integer.parseInt(ibanDestinatario));
          movimentos.add(movimento);
          Utils.sucesso("Transferência de " + valor + " para " + ibanDestinatario + " realizada com sucesso.");
        }
    }

    public ArrayList<Movimento> getMovimentos() {
        return movimentos;
    }

  @Override
  public String toString() {
      return "IBAN: " + iban +
            "\nTipo de Conta: " + tipoconta +
            "\nSaldo: " + String.format("%.2f EUR", saldo);
  }

  public String toCsv() {
        return iban + "," + nifCliente + "," + saldo + "," + tipoconta + "," + cartao.getNumero() + "," + cartao.getValidade() + "," + cartao.getCvv() + "," + cartao.getPin() + "," + cartao.isBloqueado();
  }

}
