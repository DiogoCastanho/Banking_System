package src.repository;

import src.models.Cartao;
import src.models.Cliente;
import src.models.Conta;
import src.models.TipoContaEnum;
import src.ui.ConsolaUi;
import src.utils.Utils;

import java.io.*;
import java.util.*;
import src.utils.*;
import java.time.LocalDate;

public class ContaCSVRepository {

    private static final String ficheiro = "contas.csv";
    boolean novoArquivo = !(new File(ficheiro).exists());
    Scanner sc = new Scanner(System.in);


    public void salvar(Conta conta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheiro, true))) {
          
          if(novoArquivo) {
            bw.write("IBAN,NIF,Saldo,TipoConta,NumeroCartao,Validade,CVV,Pin,Bloqueado");
            bw.newLine();
          }

          bw.write(conta.toCsv());
          bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao guardar conta");
        }
    }

    public void atualizar(Conta conta) {
        // saved the clients on the memory for rewrite all the file with changes
        List<Conta> contas = listarContas(ficheiro);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheiro))) {

            bw.write("IBAN,NIF,Saldo,TipoConta,NumeroCartao,Validade,CVV,Pin,Bloqueado");
            bw.newLine();
            for (Conta c : contas) {

                if (c.getIban().equals(conta.getIban())) {
                    bw.write(conta.toCsv());
                } else {
                    bw.write(c.toCsv());
                }

                bw.newLine();

            }

            // Utils.sucesso("Cliente " + cliente.getUtilizador() + " atualizado");

        }catch (IOException e) {
            throw new RuntimeException("Erro ao atualzar cliente");
        }
    }


    public static List<Conta> listarContas(String caminhoArquivo) {
      List<Conta> contas = new ArrayList<>();
      try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
          String cabecalho = br.readLine();
          String linha;
          while ((linha = br.readLine()) != null) {
              String[] dados = linha.split(",");
              if (dados.length == 9) {
                double saldo = Double.parseDouble(dados[2].trim());
                TipoContaEnum TipoConta = TipoContaEnum.fromNome(dados[3].trim());

                LocalDate localDate = LocalDate.parse(dados[5].trim());
                int cvv = Integer.parseInt(dados[6].trim());
                int pin = Integer.parseInt(dados[7].trim());
                boolean bloqueado = Boolean.parseBoolean(dados[8].trim());
                Conta conta = new Conta(dados[0].trim(),dados[1].trim(),saldo,TipoConta,new Cartao(dados[4].trim(), localDate, cvv, pin, bloqueado));
                contas.add(conta);
              }
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
      return contas;
    }

    public Conta buscarCartao(String numeroCartao, int pin) {
      return listarContas(ficheiro)
            .stream()
            .filter(co -> co.getCartao().getNumero().trim().equals(numeroCartao.trim()))
            .filter(co -> co.getCartao().getPin() == pin)
            .findFirst()
            .orElse(null);
    }

    public Conta buscarCartaoPorNumero(String numeroCartao) {
      return listarContas(ficheiro)
            .stream()
            .filter(co -> co.getCartao().getNumero().trim().equals(numeroCartao.trim()))
            .findFirst()
            .orElse(null);
    }

    public Conta buscarPorIban(String iban) {
      return listarContas(ficheiro)
            .stream()
            .filter(co -> co.getIban().trim().equals(iban.trim()))
            .findFirst()
            .orElse(null);
    }

    public void removerPorNifCliente(String nif) {
      
      List<Conta> contas = listarContas(ficheiro);

      List<Conta> contasRestantes = contas.stream()
            .filter(co -> !co.getNifCliente().trim().equals(nif.trim()))
            .toList();

      try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro))) {
            for (Conta c : contasRestantes) {
              pw.println(c.toCsv());
            }
      } catch (IOException e) {
          throw new RuntimeException("Erro ao remover contas do cliente.");
      }

    }

    public List<Conta> buscarContasCliente(String nif) {

      return listarContas(ficheiro).stream()
              .filter(c -> c.getNifCliente().trim().equals(nif.trim()))
              .toList();
  }

}
