package src.models;
import java.util.*;

public class Banco extends CanalAcesso {

  private String nome;
  protected List<Cliente> clientes = new ArrayList<>();

  public Banco(String nome) {
    this.nome = nome;
  }

  // getters
  public String getNome() { return nome; }
  public List<Cliente> getClientes() { return clientes; }

  public void depositarDinheiro(Conta conta, double valor) {
  }
  
  @Override
  public void verMovimentos(Conta conta, Cliente cliente) { }

}
