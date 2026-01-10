package src.repository;

import src.models.Movimento;
import src.models.TipoMove;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovimentoCSVRepository {
    
    private static final String FICHEIRO = "movimentos.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public MovimentoCSVRepository() {
        File file = new File(FICHEIRO);
        if (!file.exists()) {
            criarCabecalho();
        }
    }
    
    private void criarCabecalho() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FICHEIRO))) {
            bw.write("IBAN,DataHora,TipoMovimento,Valor,SaldoApos,IBANDestinatario");
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao criar ficheiro de movimentos");
        }
    }
    
    public void salvar(String iban, Movimento movimento) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FICHEIRO, true))) {
            bw.write(movimentoToCsv(iban, movimento));
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao guardar movimento");
        }
    }
    
    public List<Movimento> listarMovimentosPorIban(String iban) {
        List<Movimento> movimentos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO))) {
            String cabecalho = br.readLine();
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 6 && dados[0].trim().equals(iban.trim())) {
                    Movimento movimento = csvToMovimento(dados);
                    if (movimento != null) {
                        movimentos.add(movimento);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movimentos;
    }
    
    public List<Movimento> listarUltimosMovimentos(String iban, int quantidade) {
        List<Movimento> todosMovimentos = listarMovimentosPorIban(iban);
        int tamanho = todosMovimentos.size();
        if (tamanho <= quantidade) {
            return todosMovimentos;
        }
        return todosMovimentos.subList(tamanho - quantidade, tamanho);
    }
    
    private String movimentoToCsv(String iban, Movimento movimento) {
        String dataHoraStr = DATE_FORMAT.format(movimento.getDataHora());
        return iban + "," +
               dataHoraStr + "," +
               movimento.getTipoMovimento().toString() + "," +
               movimento.getValor() + "," +
               movimento.getSaldoPosOp() + "," +
               movimento.getIbanDestinatario();
    }
    
    private Movimento csvToMovimento(String[] dados) {
        try {
            Date dataHora = DATE_FORMAT.parse(dados[1].trim());
            TipoMove tipo = TipoMove.valueOf(dados[2].trim());
            double valor = Double.parseDouble(dados[3].trim());
            double saldoApos = Double.parseDouble(dados[4].trim());
            String ibanDest = dados[5].trim();
            
            return new Movimento(dataHora, valor, saldoApos, tipo, ibanDest);
        } catch (ParseException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}