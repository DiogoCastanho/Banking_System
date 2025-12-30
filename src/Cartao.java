package src;

import java.time.LocalDate;

public class Cartao {

    private String numero; // 16 caracteres
    private LocalDate validade;
    private int cvv;
    private int pin; // 4 caracteres

    public Cartao(String numero, LocalDate validade, int cvv, int pin) {
        this.numero = numero;
        this.validade = validade;
        this.cvv = cvv;
        this.pin = pin;
    }

    // getters
    public String getNumero() { return numero; }
    public LocalDate getValidade() { return validade; }
    public int getCvv() { return cvv; }
    public int getPin() { return pin; }
}
