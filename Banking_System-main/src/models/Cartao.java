package src.models;

import java.time.LocalDate;
import src.utils.*;

public class Cartao {

    private String numero; // 16 caracteres
    private LocalDate validade;
    private int cvv;
    private int pin; // 4 caracteres
    private boolean bloqueado;

    public Cartao(String numero, LocalDate validade, int cvv, int pin, boolean bloqueado) {
        this.numero = numero;
        this.validade = validade;
        this.cvv = cvv;
        this.pin = pin;
        this.bloqueado = bloqueado;
    }

    // getters
    public String getNumero() { return numero; }
    public LocalDate getValidade() { return validade; }
    public int getCvv() { return cvv; }
    public int getPin() { return pin; }
    public boolean isBloqueado() { return bloqueado; }

    public void setPin(int pin) {
        if(String.valueOf(pin).length() == 4) {
            this.pin = pin;
            Utils.sucesso("Pin alterado com sucesso!");
        }
        else {
            Utils.erro("Pin inválido. Deve ter 4 dígitos.");
        }
    }

    private void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
        if(bloqueado) {
            Utils.erro("Cartão bloqueado devido a múltiplas tentativas de login falhadas.");
        } else {
            Utils.sucesso("Cartão desbloqueado com sucesso.");
        }
    }

    public void bloquearCartao() {
        setBloqueado(true);
    }

    public void desbloquearCartao() {
        setBloqueado(false);
    }


    @Override
    public String toString() {
            String ultimos4 = numero.substring(numero.length() - 4);

            return "Cartão: **** **** **** " + ultimos4 +
                " | Validade: " + validade.getMonthValue() + "/" + validade.getYear();
        }
}
