package src;
import java.util.*;

public class Banco extends CanalAcesso {

  private String nome;
  protected ArrayList<Cliente> clientes;

  public Banco(String nome) {
    this.nome = nome;
    this.clientes = new ArrayList<>();
  }

  // getters
  public String getNome() { return nome; }
  public ArrayList<Cliente> getClientes() { return clientes; }

  public void criarCliente(Cliente cliente) {

    clientes.add(cliente);
  }

  public void listarClientes() {
    System.out.println(this);
  }

  public void depositarDinheiro(Conta conta, double valor) {
    // resto da implementação q depois 
  }

  @Override
  public void consultarSaldo(Conta conta, Cliente cliente) { }
  
  @Override
  public void verMovimentos(Conta conta, Cliente cliente) { }

  @Override
  public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append("\n=== Banco ===\n");
      sb.append("Nome do banco: ").append(nome).append("\n");
      sb.append("Número de clientes: ").append(clientes.size()).append("\n");

      if (clientes.isEmpty()) {
          sb.append("Clientes: nenhum cliente registado.\n");
      } else {
          sb.append("Clientes:\n");
          for (Cliente c : clientes) {
              sb.append(" - ").append(c.getNome()).append("\n");
          }
      }

      return sb.toString();
  }
}