package src.models;

public abstract class CanalAcesso {

  public abstract void consultarSaldo(Conta conta, Cliente cliente);

  public abstract void verMovimentos(Conta conta, Cliente cliente);

}    