package src.repository;

import src.models.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import src.utils.*;

import javax.management.RuntimeErrorException;


public class ClienteCSVRepository {

    private static final String ficheiro = "clientes.csv";
    boolean novoArquivo = !(new File(ficheiro).exists());

    public void salvar(Cliente cliente) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheiro, true))) {
            if (novoArquivo) {
                bw.write("Nome,NIF,Utilizador,Senha");
                bw.newLine();
            }
            bw.write(cliente.toCsv());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao guardar cliente");
        }
    }

    public void atualizar(Cliente cliente) {
        // saved the clients on the memory for rewrite all the file with changes
        List<Cliente> clientes = listarClientes(ficheiro);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheiro))) {

            bw.write("Nome,NIF,Utilizador,Senha");
            bw.newLine();
            for (Cliente c : clientes) {
                
                if (c.getNif().equals(cliente.getNif())) {
                    bw.write(cliente.toCsv());
                } else {
                    bw.write(c.toCsv());
                }

                bw.newLine();

            }

            Utils.sucesso("Cliente " + cliente.getNif() + " atualizado");

        }catch (IOException e) {
            throw new RuntimeException("Erro ao atualzar cliente");
        }


    }

    public static List<Cliente> listarClientes(String caminhoArquivo) {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 4) {
                    Cliente cliente = new Cliente(dados[0].trim(), dados[1].trim(), dados[2].trim(), dados[3].trim(), null);
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientes;
    }


    public Cliente buscarPorNif(String nif) {
    return listarClientes(ficheiro)
           .stream()
           .filter(c -> c.getNif().trim().equals(nif.trim()))
           .findFirst()
           .orElse(null);
    }

}
