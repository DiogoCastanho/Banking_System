package src.utils;

import src.models.Cliente;
import src.models.Conta;

public class Session {

    private static Conta currentConta;
    private static Cliente currentCliente;

    public static Conta getCurrentConta() {
        return currentConta;
    }

    public static Cliente getCurrentCliente() {
        return currentCliente;
    }

    public static void setCurrentConta(Conta conta) {
        currentConta = conta;
    }

    public static void setCurrentCliente(Cliente cliente) {
        currentCliente = cliente;
    }

    public static void clearConta() {
        currentConta = null;
    }

    public static void clearCliente() {
        currentCliente = null;
    }
}
