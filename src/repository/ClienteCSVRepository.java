package src.repository;

import src.models.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import src.utils.*;

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
            
            for (Cliente c : clientes) {
                
                if (c.getNif().equals(cliente.getNif())) {
                    bw.write(cliente.toCsv());
                } else {
                    bw.write(c.toCsv());
                }

                bw.newLine();

            }

            Utils.sucesso("Cliente " + cliente.getUtilizador() + " atualizado");

        }catch (IOException e) {
            throw new RuntimeException("Erro ao atualzar cliente");
        }
    }

    public static List<Cliente> listarClientes(String caminhoArquivo) {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String cabecalho = br.readLine();
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


    // used by search clients with nif
    public Cliente buscarPorNif(String nif) {
        return listarClientes(ficheiro)
            .stream()
            .filter(c -> c.getNif().trim().equals(nif.trim()))
            .findFirst()
            .orElse(null);
    }

    public Cliente buscarClientePorUtilizador(String utilizador) {
        return listarClientes(ficheiro)
            .stream()
            .filter(c -> c.getUtilizador().trim().equals(utilizador.trim()))
            .findFirst()
            .orElse(null);
    }

    // used by login in menu banco
    public Cliente buscarInfoCliente(String utilizador, String senha) {
        return listarClientes(ficheiro)
            .stream()
            .filter(c -> c.getUtilizador().trim().equals(utilizador.trim()))
            .filter(c -> c.getSenha().trim().equals(senha.trim()))
            .findFirst()
            .orElse(null);
    }

    public void removerPorNif(String nif) {

        List<Cliente> clientes = listarClientes(ficheiro);

        List<Cliente> clientesRestantes = clientes.stream()
            .filter(c -> !c.getNif().trim().equals(nif.trim()))
            .toList();

        try (PrintWriter pw = new PrintWriter(ficheiro)){
            for (Cliente c : clientesRestantes) {
                pw.println(c.toCsv());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover cliente pelo nif");
        }

    }

}
