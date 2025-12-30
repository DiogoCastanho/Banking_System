package src;

import java.util.*;
import java.time.LocalDate;

public class Cliente {

    private String nome;
    private String nif;
    private String utilizador;
    private String senha;
    private ArrayList<Conta> contas;


    // construtor
    public Cliente(String nome, String nif, String utilizador, String senha, Conta conta) {
        this.nome = nome;
        this.nif = nif;
        this.contas = new ArrayList<>();
        this.utilizador = utilizador;
        this.senha = senha;
    }

    // getters
    public String getNome() { return nome; }
    public String getNif() { return nif; }
    public ArrayList<Conta> getContas() { return contas; }
    public String getUtilizador() { return utilizador; }
    public String getSenha() { return senha; }

    // using only to create a new account for the existing client in system (savings account)
    public void criarConta(int numero, LocalDate validade, int cvv, int pin, String iban, double saldo, TipoContaEnum tipoconta) {
        contas.add(new Conta(new Cartao(numero, validade, cvv, pin), iban, saldo, tipoconta));
    }

    public ArrayList<Conta> listarContas() {
        return contas;
    }

    public void removerConta(Conta conta) {
        contas.remove(conta);
    }



}
