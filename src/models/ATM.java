package src.models;

public class ATM extends CanalAcesso {
    
    private String nome;

    public ATM (String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }
  
    @Override
    public void verMovimentos(Conta conta, Cliente cliente) { }
}
