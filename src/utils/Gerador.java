package src.utils;

import java.time.LocalDate;
import java.util.Random;

public class Gerador {

    private static Random random = new Random();

    // Número de cartão (16 dígitos)
    public static String gerarNumeroCartao() {
        StringBuilder numero = new StringBuilder("4");
        for (int i = 0; i < 15; i++) {
            numero.append(random.nextInt(10));
        }
        return numero.toString();
    }

    // CVV (3 dígitos)
    public static int gerarCVV() {
        return 100 + random.nextInt(900);
    }

    // PIN (4 dígitos)
    public static int gerarPIN() {
        return 1000 + random.nextInt(9000);
    }

    // Validade do cartão ( + 5 anos)
    public static LocalDate gerarValidade() {
        return LocalDate.now().plusYears(5);
    }

    // IBAN simples (PT + 23 dígitos)
    public static String gerarIBAN() {
        StringBuilder iban = new StringBuilder("PT50");
        for (int i = 0; i < 21; i++) {
            iban.append(random.nextInt(10));
        }
        return iban.toString();
    }
}
