package src.models;

  public class WebBanking extends CanalAcesso {

    /* Temos de pensar se colocamos levantarDinheiro aqui também,
    porque nas apps dos bancos tipo mbway podemos gerar codigo para levantamento ig
    */
    
    @Override
    public void consultarSaldo(Conta conta, Cliente cliente) { }
    
    @Override
    public void verMovimentos(Conta conta, Cliente cliente) { }

  }
