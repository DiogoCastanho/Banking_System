package src.models;

import java.util.*;

import src.repository.ContaCSVRepository;

public class Cliente {

    private String nome;
    private String nif;
    private String utilizador;
    private String senha;
    private Conta conta;
    private ArrayList<Conta> contas;


    // construtor
    public Cliente(String nome, String nif, String utilizador, String senha, Conta conta) {
        this.nome = nome;
        this.nif = nif;
        this.contas = new ArrayList<>();
        this.utilizador = utilizador;
        this.senha = senha;
        this.conta = conta;
    }

    // getters
    public String getNome() { return nome; }
    public String getNif() { return nif; }
    public ArrayList<Conta> getContas() { return contas; }
    public String getUtilizador() { return utilizador; }
    public String getSenha() { return senha; }
    public Conta getConta() { return conta; }

    // setters
    public void setNome(String nome) { this.nome = nome; }
    public void setUtilizador(String utilizador) { this.utilizador = utilizador; }
    public void setSenha(String senha) { this.senha = senha; }


    public String resumo() {
        return String.format(
            "Nome: %-10s| NIF: %-9s | Utilizador: %s",
            nome,
            nif,
            utilizador
        );
    }

    @Override
    public String toString() {
        return resumo();
    }

    public String toStringDetalhes(String nif) {
        StringBuilder sb = new StringBuilder();

        sb.append("----------------------------------------\n");
        sb.append("Dados do Cliente\n");
        sb.append("----------------------------------------\n");
        sb.append("Nome        : ").append(nome).append("\n");
        sb.append("NIF         : ").append(nif).append("\n");
        sb.append("Utilizador  : ").append(utilizador).append("\n");
        sb.append("Senha       : ").append(senha).append("\n");
        sb.append("----------------------------------------");

        return sb.toString();

    }

    public String toCsv() {
        return nome + "," + nif + "," + utilizador + "," + senha;
    }

}
