package src.models;

import java.util.*;

// enum para o tipo de movimento
enum TipoMove {
    levamentar,
    depositar,
    enviar,
    receber
}

public class Movimento {
  
    private Date data_hora;
    private double valor;
    private double saldo_apos_operacao;
    private TipoMove tipo_movimento;
    private int ibanDestinatario;

    // construtor
    public Movimento(Date data_hora, float valor, float saldo_apos_operacao, TipoMove tipo_movimento, int ibanDestinatario){
        this.data_hora = data_hora;
        this.valor = valor;
        this.saldo_apos_operacao = saldo_apos_operacao;
        this.tipo_movimento = tipo_movimento;
        this.ibanDestinatario = ibanDestinatario;
    }
    
    // getters
    public Date getDataHora() { return data_hora; }
    public double getValor() { return valor; }
    public double getSaldoPosOp() { return saldo_apos_operacao; }
    public TipoMove getTipoMovimento() { return tipo_movimento; }
    public int getIbanDestinatario() { return ibanDestinatario; }
}
