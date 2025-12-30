package src.utils;

import java.util.Scanner;

public class Utils {
  public static final String RESET = "\u001B[0m";
  public static final String VERMELHO = "\u001B[31m";
  public static final String VERDE = "\u001B[32m";
  public static final String AMARELO = "\u001B[33m";

  
  public static void limparTela() {
    /* \033[H → move o cursor para o canto superior esquerdo
    \033[2J → limpa a tela */
    System.out.print("\033[H\033[2J");
    System.out.flush();

  }

  public static void erro(String mensagem) {
    System.out.println(VERMELHO + mensagem + RESET);
  }

  public static void sucesso(String mensagem) {
      System.out.println(VERDE + mensagem + RESET);
  }

  // client validations when user want create a client
  public static String lerNome(Scanner sc) {
    String nome;
    do {
      System.out.print("Nome: ");
      nome = sc.next();

      if (nome.isBlank()) {
          erro("Erro: O nome não pode estar vazio.");
      }
    } while(nome.isBlank());

    return nome;
  }

  public static String lerNif(Scanner sc) {
    String nif;
    do {
      System.out.print("NIF: ");
      nif = sc.next();

     if (!nif.matches("\\d{9}")) {
        erro("Erro: O NIF tem de ter exatamente 9 números.");
      }
    } while (!nif.matches("\\d{9}"));
    
    return nif;
  }

  public static String lerUtiizador(Scanner sc) {
    String utilizador;
    do {
      System.out.print("Nome de utilizador: ");
      utilizador = sc.next();

     if (utilizador.isBlank()) {
        erro("Erro: O nome de utilizador não pode estar vazio.");
      }
    } while (utilizador.isBlank());
    
    return utilizador;
  }

  public static String lerPalavraP(Scanner sc) {
    String senha;
    do {
      System.out.print("Palavra-passe (mín. 4 caracteres): ");
      senha = sc.next();

      if (senha.length() < 4) {
        erro("Erro: Palavra-passe demasiado curta.");
      }
      } while (senha.length() < 4);

      return senha;
  }

}
