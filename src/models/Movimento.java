package src.models;

import java.util.*;

public class Movimento {
  
    private Date data_hora;
    private double valor;
    private double saldo_apos_operacao;
    private TipoMove tipo_movimento;
    private String ibanDestinatario;

    public Movimento(Date data_hora, double valor, double saldo_apos_operacao, TipoMove tipo_movimento, String ibanDestinatario){
        this.data_hora = data_hora;
        this.valor = valor;
        this.saldo_apos_operacao = saldo_apos_operacao;
        this.tipo_movimento = tipo_movimento;
        this.ibanDestinatario = ibanDestinatario;
    }
    
    public Date getDataHora() { return data_hora; }
    public double getValor() { return valor; }
    public double getSaldoPosOp() { return saldo_apos_operacao; }
    public TipoMove getTipoMovimento() { return tipo_movimento; }
    public String getIbanDestinatario() { return ibanDestinatario; }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("\n--- Detalhes da Operação ---\n")
            .append(String.format("  Data/Hora:     %s\n", data_hora))
            .append(String.format("  Tipo:          %s\n", tipo_movimento))
            .append(String.format("  Valor:         %.2f\n", valor))
            .append(String.format("  IBAN Dest.:    %s\n", (ibanDestinatario != null ? ibanDestinatario : "N/A")))
            .append(String.format("  Saldo Final:   %.2f\n", saldo_apos_operacao))
            .append("---------------------------")
            .toString();
    }
}