package src.repository;

import src.models.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
