package src.models;

import java.util.ArrayList;

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
  public Cartao getCartao() { return cartao; }
  public String getIban() { return iban; }
  public double getSaldo() { return saldo; }
  public TipoContaEnum getTipoConta() { return tipoconta; }

  // public void tranferirEntreContas(Conta contaDestino, valor){}

  // public void listarMovimentos() {}

  @Override
  public String toString() {
      String ibanMascarado = iban.substring(0, 4) + "*************" +
                            iban.substring(iban.length() - 4);

      return "IBAN: " + ibanMascarado +
            "\nTipo de Conta: " + tipoconta +
            "\nSaldo: " + String.format("%.2f €", saldo) +
            "\n" + cartao;
  }

  public String toCsv() {
        return iban + "," + nifCliente + "," + saldo + "," + tipoconta + "," + cartao.getNumero() + "," + cartao.getValidade() + "," + cartao.getCvv() + "," + cartao.getPin();
  }

}
