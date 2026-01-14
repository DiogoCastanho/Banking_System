package src.models;

import java.util.ArrayList;
import java.util.Scanner;

import src.repository.MovimentoCSVRepository;
import src.ui.ConsolaUi;
import src.utils.*;

public class Conta {
  
  protected String iban;
  protected String nifCliente;
  protected double saldo;
  protected TipoContaEnum tipoconta;
  protected ArrayList<Movimento> movimentos;
  protected Cartao cartao;
  private MovimentoCSVRepository movimentoRepo;

  public Conta(String iban, String nifCliente, double saldo, TipoContaEnum tipoconta, Cartao cartao) {
    this.iban   = iban;
    this.nifCliente = nifCliente;
    this.saldo  = saldo;
    this.tipoconta = tipoconta;
    this.cartao = cartao;
    this.movimentos = new ArrayList<>();
    this.movimentoRepo = new MovimentoCSVRepository();
  }

  public String getNifCliente() { return nifCliente; }
  public String getIban() { return iban; }
  public double getSaldo() { return saldo; }
  public TipoContaEnum getTipoConta() { return tipoconta; }
  public Cartao getCartao() { return cartao; }

  // setters
  public double adicionarSaldo(double valor) {
    return this.saldo += valor;
  }

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
        Movimento movimento = new Movimento(
            new java.util.Date(), 
            valor, 
            saldo, 
            TipoMove.levantar,
            "0"
        );
        movimentos.add(movimento);
        movimentoRepo.salvar(iban, movimento);
        Utils.sucesso("Levantamento de " + valor + " EUR realizado com sucesso.");
        ConsolaUi.pausa(sc);
    }

    public boolean levantarDinheiroSemPausa(double valor) {
        if (valor <= 0) {
            return false;
        }
        if (valor > saldo) {
            return false;
        }
        saldo -= valor;
        Movimento movimento = new Movimento(
            new java.util.Date(),
            valor,
            saldo,
            TipoMove.levantar,
            "0"
        );
        movimentos.add(movimento);
        movimentoRepo.salvar(iban, movimento);
        return true;
    }

    public void depositarDinheiro(double valor) {
        if (valor <= 0) {
            Utils.erro("Valor inválido para depósito.");
            return;
        }
        
        saldo += valor;
        
        Movimento movimento = new Movimento(
            new java.util.Date(), 
            valor, 
            saldo, 
            TipoMove.depositar, 
            "0"
        );
        movimentos.add(movimento);
        movimentoRepo.salvar(iban, movimento);
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
