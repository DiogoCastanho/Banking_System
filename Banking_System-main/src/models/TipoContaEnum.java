package src.models;

public enum TipoContaEnum {
  CONTA_ORDEM("Conta Ordem"),
  CONTA_POUPANCA("Conta Poupanca");

  private final String nome;

  TipoContaEnum(String nome) {
    this.nome = nome;
  }

  public String getNome() {
    return nome;  
  }

  //converter de String para TipoContaEnum
  public static TipoContaEnum fromNome(String nome) {
        for (TipoContaEnum tipo : values()) {
            if (tipo.nome.equalsIgnoreCase(nome.trim())) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de conta inválido: " + nome);
    }

  @Override
  public String toString() {
    return nome;
  }
}
