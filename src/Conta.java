package src;

import java.util.ArrayList;

public class Conta {
  
  protected Cartao cartao;
  protected String iban;
  protected double saldo;
  protected TipoContaEnum tipoconta;
  protected ArrayList<Movimento> movimentos;

  // construtor
  public Conta(Cartao cartao, String iban, double saldo, TipoContaEnum tipoconta) {
    this.cartao = cartao;
    this.iban   = iban;
    this.saldo  = saldo;
    this.tipoconta = tipoconta;
    this.movimentos = new ArrayList<>();
  }

  // getters
  public Cartao getCartao() { return cartao; }
  public String getIban() { return iban; }
  public double getSaldo() { return saldo; }

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

}
