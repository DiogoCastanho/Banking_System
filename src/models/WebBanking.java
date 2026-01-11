package src.models;

  public class WebBanking extends CanalAcesso {
    
    private String nome;

    public WebBanking (String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }
  }
