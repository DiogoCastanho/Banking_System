package src.repository;

import src.models.Cliente;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteCSVRepository {

    private static final String ficheiro = "clientes.csv";

    public void salvar(Cliente cliente) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheiro, true))) {
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
            br.readLine(); // Pular cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(","); // separar campos
                if (dados.length == 4) { // verificar se tem todos os campos
                    Cliente cliente = new Cliente(dados[0], dados[1], dados[2], null, null);
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    // public boolean procurarPorNif(String nif) {
    //     return listarClientes(String).stream().anyMatch(c -> c.getNif().equals(nif));
    // }
}
