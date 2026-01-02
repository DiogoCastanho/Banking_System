package src.utils;

import src.models.Conta;

public class Session {

    private static Conta currentConta;

    public static Conta getCurrentConta() {
        return currentConta;
    }

    public static void setCurrentConta(Conta conta) {
        currentConta = conta;
    }

    public static void clear() {
        currentConta = null;
    }
}
