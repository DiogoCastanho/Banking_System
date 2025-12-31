package src.models;

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

    @Override
        public String toString() {
            String ultimos4 = numero.substring(numero.length() - 4);

            return "Cartão: **** **** **** " + ultimos4 +
                " | Validade: " + validade.getMonthValue() + "/" + validade.getYear();
        }

}
