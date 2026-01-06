package src.utils;

import java.util.Scanner;

public class Utils {

    public static final String RESET = "\u001B[0m";
    public static final String VERMELHO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARELO = "\u001B[33m";

    // ================= UI =================

    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void erro(String mensagem) {
        System.out.println(VERMELHO + mensagem + RESET);
    }

    public static void sucesso(String mensagem) {
        System.out.println(VERDE + mensagem + RESET);
    }

    public static void aviso(String mensagem) {
        System.out.println(AMARELO + mensagem + RESET);
    }

    public static String lerTextoObrigatorio(Scanner sc, String mensagem) {
        String texto;
        do {
            System.out.print(mensagem);
            texto = sc.nextLine().trim();

            if (texto.isEmpty()) {
                erro("Este campo não pode estar vazio.");
            }
        } while (texto.isEmpty());

        return texto;
    }

    public static int lerOpcao(Scanner sc, String mensagem) {
        int opcao;
        do {
            System.out.print(mensagem);
            opcao = sc.nextInt();

            if (opcao < 0) {
                erro("Campo inválido.");
            }
        } while (opcao < 0);

        return opcao;
    }

    public static String lerTextoParaAtualizarCliente(Scanner sc, String mensagem, String valorAtual) {
        String texto;

        System.out.print(mensagem);
        texto = sc.nextLine().trim();

        if (texto.isBlank()) {
            aviso("Mantido");
            return valorAtual;
        }

        sucesso("Atualizado");
        return texto;
        
    }

    public static String lerSenha(Scanner sc, String mensagem) {
        String senha;
        do {
            System.out.print(mensagem);
            senha = sc.nextLine().trim();

            if (senha.isEmpty()) {
                erro("A palavra-passe não pode estar vazia.");
            }
        } while (senha.isEmpty());

        return senha;
    }

    public static String lerSenhaParaAtualizarCliente(Scanner sc, String mensagem, String valorAtual) {
        String senha;
        
        System.out.print(mensagem);
        senha = sc.nextLine().trim();

        if (senha.isBlank()) {
            aviso("Mantida");
            return valorAtual;
        }

        sucesso("Atualizada");
        return senha;

    }
}
