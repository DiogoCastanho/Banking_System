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
        ClienteCSVRepository clienteRepository = new ClienteCSVRepository();
        Scanner sc = new Scanner(System.in);

        if (!loginAdmin(sc)) {
            Utils.erro("Login falhou. A sair...");
            return;
        }

        int opcao;
        do {
            Utils.limparTela();
            ConsolaUi.titulo("Menu Banco");

            ConsolaUi.titulo("Gestão de Clientes");
            System.out.println("1 - Criar Cliente");
            System.out.println("2 - Listar Clientes");
            System.out.println("3 - Editar Cliente");
            System.out.println("4 - Remover Cliente");

            ConsolaUi.secao("Gestão de Contas");
            System.out.println("5 - Criar Conta");
            System.out.println("6 - Listar Contas");
            System.out.println("7 - Remover Cliente");

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
                            System.out.println(clienteEdit.toStringDetalhes());
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
                    ContaCSVRepository contaRepository = new ContaCSVRepository();
                    List<Conta> contas = contaRepository.listarContas("contas.csv");
                    ConsolaUi.secao("Lista de Contas");
                    contas.forEach(c -> System.out.println(c.toString()));

                    break;
                case 7:
                    System.out.println("\n== Remover Conta");
                    break;
                case 8:
                    System.out.println("\n== Desbloquear Cartão (ATM)");
                    break;
                case 0:
                    System.out.println("A voltar ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    // Login method
    private static boolean loginAdmin(Scanner sc) {
        final String ADMIN_USER = "admin";
        final String ADMIN_PASS = "admin";

        ConsolaUi.titulo("Login");
        System.out.print("Utilizador: ");
        String user = sc.nextLine();
        System.out.print("Palavra-passe: ");
        String pass = sc.nextLine();

        if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
            Utils.sucesso("Login bem-sucedido!");
            return true;
        } else {
            Utils.erro("Utilizador ou palavra-passe incorretos!");
            return false;
        }
    }
}
