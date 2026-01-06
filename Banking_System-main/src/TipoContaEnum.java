package src;

public enum TipoContaEnum {
  CONTA_ORDEM("Conta Ordem"),
  CONTA_POUPANCA("Conta Poupança");

  private final String nome;

  TipoContaEnum(String nome) {
    this.nome = nome;
  }

  public String getNome() {
    return nome;  
  }

  @Override
  public String toString() {
    return nome;
  }
}
