package src.repository;

import src.models.Conta;
import java.io.*;
import java.util.*;

public class ContaCSVRepository {

    private static final String ficheiro = "contas.csv";
    boolean novoArquivo = !(new File(ficheiro).exists());


    public void salvar(Conta conta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheiro, true))) {
          
          if(novoArquivo) {
            bw.write("IBAN,NIF,NumeroCartao,Validade,CVV,Pin,Saldo,TipoConta");
            bw.newLine();
          }

          bw.write(conta.toCsv());
          bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao guardar conta");
        }
    }

    public static List<Conta> listarContas(String caminhoArquivo) {
        List<Conta> contas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(","); // separar campos
                if (dados.length == 5) { // verificar se tem todos os campos
                    Conta conta = new Conta(dados[0], dados[1], 0, null, null);
                    contas.add(conta);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contas;
    }
}
