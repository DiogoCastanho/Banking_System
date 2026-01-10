package src.menus;

import src.models.Banco;
import src.models.Cliente;
import src.models.Conta;
import src.repository.*;

import java.util.*;
import src.utils.*;
import src.ui.ConsolaUi;

public class menuBanco {

    public static void showMenuBanco() {
        Banco banco = new Banco("Banco do mell");
        ClienteService clienteService = new ClienteService(); // used by create client section
        ContaService contaService = new ContaService(); // used by create additional account
        ContaCSVRepository contaRepository = new ContaCSVRepository();
        ClienteCSVRepository clienteRepository = new ClienteCSVRepository();
        Scanner sc = new Scanner(System.in);

        int opcao;
        do {
            Utils.limparTela();
            ConsolaUi.titulo("Menu Banco");

            ConsolaUi.secao("Gestão de Clientes");
            System.out.println("1 - Criar Cliente");
            System.out.println("2 - Listar Clientes");
            System.out.println("3 - Editar Cliente");
            System.out.println("4 - Remover Cliente");

            ConsolaUi.secao("Gestão de Contas");
            System.out.println("5 - Criar Conta");
            System.out.println("6 - Listar Contas");
            System.out.println("7 - Remover Conta");

            ConsolaUi.secao("Cartões e Acessos");
            System.out.println("8 - Desbloquear Cartão (ATM)");
            System.out.println("0 - Logout");

            ConsolaUi.linha();
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();
            sc.nextLine(); // limpar buffer
            
            switch (opcao) {
                case 1:

                    ConsolaUi.titulo("Criar Cliente");

                    String nome = Utils.lerTextoObrigatorio(sc, "Nome: ");
                    String nif = Utils.lerTextoObrigatorio(sc, "NIF: ");
                    String utilizador = Utils.lerTextoObrigatorio(sc, "Utilizador: ");
                    String senha = Utils.lerSenha(sc, "Palavra-passe: ");

                    try {
                        Cliente cliente = clienteService.criarCliente(
                                nome, nif, utilizador, senha
                        );

                        System.out.println("\nConta Associada:\n" + cliente.getConta());
                        Utils.sucesso("Cliente criado com sucesso!");

                        ConsolaUi.pausa(sc);

                    } catch (IllegalArgumentException e) {
                        Utils.erro(e.getMessage());
                    }
                    break;
                case 2:
                    List<Cliente> clientes = clienteRepository.listarClientes("clientes.csv");
                    System.out.println("\n== Lista de Clientes");
                    clientes.forEach(c -> System.out.println(c.toString()));

                    if (clientes.size() == 0) {
                        Utils.aviso("Ainda não existem clientes registados");
                    }

                    ConsolaUi.pausa(sc);
                    break;
                case 3:
                    System.out.println("\n== Editar Cliente");
                    
                    String nifCl = Utils.lerTextoObrigatorio(sc, "Introduza o NIF do cliente: ");

                    try {
                        Cliente clienteEdit = clienteRepository.buscarPorNif(nifCl);

                        if (clienteEdit == null) {
                            throw new IllegalArgumentException("Cliente não encontrado.");
                        } else {
                            Utils.sucesso("Cliente encontrado");
                            System.out.println(clienteEdit.toStringDetalhes(nifCl));
                            ConsolaUi.pausa(sc);

                            // request is made to the user to change their data
                            String nomeC = Utils.lerTextoParaAtualizarCliente(sc, "Atualizar nome (ENTER para manter): ", clienteEdit.getNome());
                            String nifC = Utils.lerTextoParaAtualizarCliente(sc, "*NIF Impossível editar", clienteEdit.getNif());
                            String utilizadorC = Utils.lerTextoParaAtualizarCliente(sc, "Atualizar utilizador (ENTER para manter): ", clienteEdit.getUtilizador());
                            String senhaC = Utils.lerSenhaParaAtualizarCliente(sc, "Atualizar senha (ENTER para manter): ",clienteEdit.getSenha());
                            ConsolaUi.pausa(sc);

                            Cliente clienteAtualizado = clienteService.editarCliente(nomeC, nifC, utilizadorC, senhaC);
                            ConsolaUi.pausa(sc);

                        }
                    } catch (IllegalArgumentException e) {
                        Utils.erro(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("\n== Remover Cliente");
                    String nifR = Utils.lerTextoObrigatorio(sc, "Introduza o NIF do cliente: ");

                    try {
                        Cliente cliente = clienteRepository.buscarPorNif(nifR);

                        Utils.sucesso("Cliente encontrado");
                        System.out.println(cliente.toStringDetalhes(nifR));

                        String confirmação = Utils.lerTextoObrigatorio(sc, 
                            "Tem a certeza que deseja remover este cliente? (S/N): ");

                        if (confirmação.equalsIgnoreCase("S")) {
                            clienteService.removerCliente(nifR);
                        } else {
                            Utils.aviso("Operação cancelada.");
                        }

                        ConsolaUi.pausa(sc);

                    } catch (IllegalArgumentException e) {
                        Utils.erro(e.getMessage());
                        ConsolaUi.pausa(sc);
                    }
                    break;
                case 5:

                    ConsolaUi.titulo("Criar Conta Adicional");

                    String nifCliente = Utils.lerTextoObrigatorio(sc, "Introduza o NIF do cliente: ");

                    try {
                        Conta conta = contaService.criarContaAdicional(nifCliente);

                        if (conta == null) {
                            Utils.erro("Erro ao criar conta adicional");
                        } else {
                            System.out.println("\nConta criada:\n" + conta);
                            Utils.sucesso("Conta adicional criada com sucesso!");

                            ConsolaUi.pausa(sc);
                        }

                        

                    } catch (IllegalArgumentException e) {
                        Utils.erro(e.getMessage());
                    }
                    break;
                case 6:
                    ConsolaUi.titulo("Listar Contas");
                    
                    List<Conta> contas = contaRepository.listarContas("contas.csv");

                    ConsolaUi.secao("Lista de Contas");
                    contas.forEach(c -> System.out.println(c.toString()));

                    if (contas.size() == 0) {
                        Utils.aviso("Ainda não existem contas registadas");
                    }

                    ConsolaUi.pausa(sc);
                    break;
                case 7:
                    System.out.println("\n== Remover Conta");
                    String nifRc = Utils.lerTextoObrigatorio(sc, "Introduza o NIF do cliente: ");

                    try {
                        List<Conta> contasCliente = contaRepository.buscarContasCliente(nifRc);

                                            
                        if (contasCliente == null || contasCliente.isEmpty()) {
                            Utils.aviso("O cliente não possui contas associadas.");
                            ConsolaUi.pausa(sc);
                            break;
                        }

                        // show account
                        ConsolaUi.mostrarContas(contasCliente, nifRc, sc);

                        // choose the option
                        Conta contaEscolhida = contaService.escolherConta(contasCliente);
                        
                        contaService.buscarContaParaRemover(contaEscolhida);
                        ConsolaUi.pausa(sc);

                    } catch (Exception e) {
                        Utils.erro(e.getMessage());
                    }

                    break;
                case 8:
                    System.out.println("\n== Desbloquear Cartão (ATM)");
                    String numeroCartao = Utils.lerTextoObrigatorio(sc, "Nº Do Cartão: ");
                    src.utils.Session.setCurrentConta(contaRepository.buscarCartaoPorNumero(numeroCartao));
                    System.out.println(src.utils.Session.getCurrentConta().getCartao().toString());

                    if (src.utils.Session.getCurrentConta().getCartao().isBloqueado() == true) {
                        src.utils.Session.getCurrentConta().getCartao().desbloquearCartao();
                    } else {
                        Utils.aviso("O cartão apresentado, não se encontra bloqueado.");
                    }

                    contaRepository.atualizar(src.utils.Session.getCurrentConta());
                    ConsolaUi.pausa(sc);
                    break;
                case 0:
                    Utils.sucesso("A voltar ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    ConsolaUi.pausa(sc);
            }

        } while (opcao != 0);
    }
}
