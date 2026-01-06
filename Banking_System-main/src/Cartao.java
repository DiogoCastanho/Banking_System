package src;

import java.time.LocalDate;

public class Cartao {

    private int numero; // 16 caracteres
    private LocalDate validade;
    private int cvv;
    private int pin; // 4 caracteres

    public Cartao(int numero, LocalDate validade, int cvv, int pin) {
        this.numero = numero;
        this.validade = validade;
        this.cvv = cvv;
        this.pin = pin;
    }

    // getters
    public int getNumero() { return numero; }
    public LocalDate getValidade() { return validade; }
    public int getCvv() { return cvv; }
    public int getPin() { return pin; }
}
