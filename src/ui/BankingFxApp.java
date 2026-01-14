package src.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import src.models.Cliente;
import src.models.Conta;
import src.models.Movimento;
import src.models.TipoContaEnum;
import src.models.TipoMove;
import src.repository.ClienteCSVRepository;
import src.repository.ClienteService;
import src.repository.ContaCSVRepository;
import src.repository.ContaService;
import src.repository.MovimentoCSVRepository;

public class BankingFxApp extends Application {

    private final ClienteCSVRepository clienteRepo = new ClienteCSVRepository();
    private final ContaCSVRepository contaRepo = new ContaCSVRepository();
    private final ClienteService clienteService = new ClienteService();
    private final ContaService contaService = new ContaService();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sistema Bancário");
        stage.setScene(buildMainMenuScene());
        stage.show();
    }

    private Scene buildMainMenuScene() {
        Label title = new Label("Sistema Bancário");
        title.setFont(Font.font(20));

        Button bancoButton = new Button("Banco");
        bancoButton.setMaxWidth(Double.MAX_VALUE);
        bancoButton.setOnAction(event -> handleBancoLogin());

        Button atmButton = new Button("ATM");
        atmButton.setMaxWidth(Double.MAX_VALUE);
        atmButton.setOnAction(event -> handleAtm());

        Button webButton = new Button("WebBanking");
        webButton.setMaxWidth(Double.MAX_VALUE);
        webButton.setOnAction(event -> handleWebBanking());

        Button exitButton = new Button("Sair");
        exitButton.setMaxWidth(Double.MAX_VALUE);
        exitButton.setOnAction(event -> ((Stage) exitButton.getScene().getWindow()).close());

        VBox root = new VBox(12, title, new Separator(), bancoButton, atmButton, webButton, exitButton);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 320, 300);
    }

    private void handleBancoLogin() {
        while (true) {
            Optional<String> userOpt = FxDialogs.promptText("Login Banco", "Autenticação", "Utilizador");
            if (userOpt.isEmpty()) {
                return;
            }
            Optional<String> passOpt = FxDialogs.promptPassword("Login Banco", "Autenticação", "Palavra-passe");
            if (passOpt.isEmpty()) {
                return;
            }

            String utilizador = userOpt.get();
            String senha = passOpt.get();

            if (utilizador.equals("admin") && senha.equals("admin")) {
                FxDialogs.info("Banco", "Autenticação bem-sucedida! Bem-vindo Admin.");
                handleAdminMenu();
                return;
            }

            Cliente cliente = clienteRepo.buscarInfoCliente(utilizador, senha);
            if (cliente != null) {
                FxDialogs.info("Banco", "Autenticação bem-sucedida! Bem-vindo(a), " + cliente.getNome());
                handleBancoClienteMenu(cliente);
                return;
            }

            FxDialogs.error("Banco", "Credenciais inválidas. Tente novamente.");
        }
    }

    private void handleAdminMenu() {
        List<String> options = List.of(
                "Criar Cliente",
                "Listar Clientes",
                "Editar Cliente",
                "Remover Cliente",
                "Criar Conta",
                "Listar Contas",
                "Remover Conta",
                "Desbloquear Cartão (ATM)",
                "Logout"
        );

        while (true) {
            Optional<String> choiceOpt = FxDialogs.promptChoice(
                    "Menu Banco",
                    "Selecione uma opção",
                    "Operações disponíveis",
                    options
            );

            if (choiceOpt.isEmpty() || choiceOpt.get().equals("Logout")) {
                return;
            }

            switch (choiceOpt.get()) {
                case "Criar Cliente":
                    criarCliente();
                    break;
                case "Listar Clientes":
                    listarClientes();
                    break;
                case "Editar Cliente":
                    editarCliente();
                    break;
                case "Remover Cliente":
                    removerCliente();
                    break;
                case "Criar Conta":
                    criarContaAdicional();
                    break;
                case "Listar Contas":
                    listarContas();
                    break;
                case "Remover Conta":
                    removerConta();
                    break;
                case "Desbloquear Cartão (ATM)":
                    desbloquearCartao();
                    break;
                default:
                    FxDialogs.warning("Banco", "Opção inválida.");
            }
        }
    }

    private void criarCliente() {
        Optional<String> nomeOpt = FxDialogs.promptText("Criar Cliente", "Novo Cliente", "Nome");
        if (nomeOpt.isEmpty()) {
            return;
        }
        Optional<String> nifOpt = FxDialogs.promptText("Criar Cliente", "Novo Cliente", "NIF");
        if (nifOpt.isEmpty()) {
            return;
        }
        Optional<String> utilizadorOpt = FxDialogs.promptText("Criar Cliente", "Novo Cliente", "Utilizador");
        if (utilizadorOpt.isEmpty()) {
            return;
        }
        Optional<String> senhaOpt = FxDialogs.promptPassword("Criar Cliente", "Novo Cliente", "Palavra-passe");
        if (senhaOpt.isEmpty()) {
            return;
        }

        try {
            Cliente cliente = clienteService.criarCliente(
                    nomeOpt.get(),
                    nifOpt.get(),
                    utilizadorOpt.get(),
                    senhaOpt.get()
            );

            FxDialogs.showScrollable(
                    "Cliente criado",
                    "Conta Associada",
                    cliente.getConta().toString()
            );
        } catch (IllegalArgumentException e) {
            FxDialogs.error("Criar Cliente", e.getMessage());
        }
    }

    private void listarClientes() {
        List<Cliente> clientes = ClienteCSVRepository.listarClientes("clientes.csv");

        if (clientes.isEmpty()) {
            FxDialogs.warning("Clientes", "Ainda não existem clientes registados.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Cliente cliente : clientes) {
            sb.append(cliente.toString()).append("\n");
        }

        FxDialogs.showScrollable("Clientes", "Lista de Clientes", sb.toString());
    }

    private void editarCliente() {
        Optional<String> nifOpt = FxDialogs.promptText("Editar Cliente", "Introduza o NIF do cliente", "NIF");
        if (nifOpt.isEmpty()) {
            return;
        }

        Cliente cliente = clienteRepo.buscarPorNif(nifOpt.get());
        if (cliente == null) {
            FxDialogs.error("Editar Cliente", "Cliente não encontrado.");
            return;
        }

        Optional<String> nomeOpt = FxDialogs.promptText(
                "Editar Cliente",
                "Atualizar nome (deixe vazio para manter)",
                "Nome",
                cliente.getNome()
        );
        if (nomeOpt.isEmpty()) {
            return;
        }
        Optional<String> utilizadorOpt = FxDialogs.promptText(
                "Editar Cliente",
                "Atualizar utilizador (deixe vazio para manter)",
                "Utilizador",
                cliente.getUtilizador()
        );
        if (utilizadorOpt.isEmpty()) {
            return;
        }
        Optional<String> senhaOpt = FxDialogs.promptPassword(
                "Editar Cliente",
                "Atualizar senha (deixe vazio para manter)",
                "Senha"
        );
        if (senhaOpt.isEmpty()) {
            return;
        }

        String nome = nomeOpt.get().isEmpty() ? cliente.getNome() : nomeOpt.get();
        String utilizador = utilizadorOpt.get().isEmpty() ? cliente.getUtilizador() : utilizadorOpt.get();
        String senha = senhaOpt.get().isEmpty() ? cliente.getSenha() : senhaOpt.get();

        clienteService.editarCliente(nome, cliente.getNif(), utilizador, senha);
        FxDialogs.info("Editar Cliente", "Cliente atualizado com sucesso.");
    }

    private void removerCliente() {
        Optional<String> nifOpt = FxDialogs.promptText("Remover Cliente", "Introduza o NIF do cliente", "NIF");
        if (nifOpt.isEmpty()) {
            return;
        }

        Cliente cliente = clienteRepo.buscarPorNif(nifOpt.get());
        if (cliente == null) {
            FxDialogs.error("Remover Cliente", "Cliente não encontrado.");
            return;
        }

        boolean confirmar = FxDialogs.confirm(
                "Remover Cliente",
                "Confirmação",
                "Tem a certeza que deseja remover este cliente?"
        );

        if (!confirmar) {
            FxDialogs.warning("Remover Cliente", "Operação cancelada.");
            return;
        }

        try {
            clienteService.removerCliente(nifOpt.get());
            FxDialogs.info("Remover Cliente", "Operação concluída.");
        } catch (IllegalArgumentException e) {
            FxDialogs.error("Remover Cliente", e.getMessage());
        }
    }

    private void criarContaAdicional() {
        Optional<String> nifOpt = FxDialogs.promptText("Criar Conta", "Introduza o NIF do cliente", "NIF");
        if (nifOpt.isEmpty()) {
            return;
        }

        Optional<String> tipoOpt = FxDialogs.promptChoice(
                "Criar Conta",
                "Escolha o tipo de conta",
                "Tipo de Conta",
                List.of("Conta Ordem", "Conta Poupança")
        );
        if (tipoOpt.isEmpty()) {
            return;
        }

        TipoContaEnum tipoConta = tipoOpt.get().equals("Conta Ordem")
                ? TipoContaEnum.CONTA_ORDEM
                : TipoContaEnum.CONTA_POUPANCA;

        try {
            Conta conta = contaService.criarContaAdicional(nifOpt.get(), tipoConta);
            FxDialogs.showScrollable("Conta criada", "Conta criada com sucesso", conta.toString());
        } catch (IllegalArgumentException e) {
            FxDialogs.error("Criar Conta", e.getMessage());
        }
    }

    private void listarContas() {
        List<Conta> contas = ContaCSVRepository.listarContas("contas.csv");

        if (contas.isEmpty()) {
            FxDialogs.warning("Contas", "Ainda não existem contas registadas.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Conta conta : contas) {
            sb.append(conta.toString()).append("\n\n");
        }

        FxDialogs.showScrollable("Contas", "Lista de Contas", sb.toString());
    }

    private void removerConta() {
        Optional<String> nifOpt = FxDialogs.promptText("Remover Conta", "Introduza o NIF do cliente", "NIF");
        if (nifOpt.isEmpty()) {
            return;
        }

        List<Conta> contasCliente = contaRepo.buscarContasCliente(nifOpt.get());
        if (contasCliente == null || contasCliente.isEmpty()) {
            FxDialogs.warning("Remover Conta", "O cliente não possui contas associadas.");
            return;
        }

        Conta contaEscolhida = selecionarConta(contasCliente, "Remover Conta");
        if (contaEscolhida == null) {
            return;
        }

        if (contaEscolhida.getSaldo() > 0) {
            FxDialogs.error(
                    "Remover Conta",
                    "Não é possível remover a conta com saldo disponível."
            );
            return;
        }

        boolean confirmar = FxDialogs.confirm(
                "Remover Conta",
                "Confirmação",
                "Confirmar remoção da conta " + contaEscolhida.getIban() + "?"
        );

        if (!confirmar) {
            return;
        }

        contaService.buscarContaParaRemover(contaEscolhida);
        FxDialogs.info("Remover Conta", "Conta removida com sucesso.");
    }

    private void desbloquearCartao() {
        Optional<String> numeroOpt = FxDialogs.promptText("Desbloquear Cartão", "Número do cartão", "Nº do Cartão");
        if (numeroOpt.isEmpty()) {
            return;
        }

        Conta conta = contaRepo.buscarCartaoPorNumero(numeroOpt.get());
        if (conta == null) {
            FxDialogs.error("Desbloquear Cartão", "Cartão não encontrado.");
            return;
        }

        if (conta.getCartao().isBloqueado()) {
            conta.getCartao().desbloquearCartao();
            contaRepo.atualizar(conta);
            FxDialogs.info("Desbloquear Cartão", "Cartão desbloqueado com sucesso.");
        } else {
            FxDialogs.warning("Desbloquear Cartão", "O cartão não se encontra bloqueado.");
        }
    }

    private void handleBancoClienteMenu(Cliente cliente) {
        List<String> options = List.of(
                "Aceder Conta",
                "Logout"
        );

        while (true) {
            Optional<String> choiceOpt = FxDialogs.promptChoice(
                    "Menu Cliente",
                    "Cliente: " + cliente.getNome(),
                    "Operações",
                    options
            );

            if (choiceOpt.isEmpty() || choiceOpt.get().equals("Logout")) {
                return;
            }

            if (choiceOpt.get().equals("Aceder Conta")) {
                Conta conta = selecionarConta(contaRepo.buscarContasCliente(cliente.getNif()), "Selecionar Conta");
                if (conta != null) {
                    handleContaCliente(conta);
                }
            }
        }
    }

    private void handleContaCliente(Conta conta) {
        List<String> options = List.of(
                "Consultar Saldo",
                "Depositar dinheiro",
                "Levantar dinheiro",
                "Fazer Transferências",
                "Sair da Conta"
        );

        while (true) {
            Optional<String> choiceOpt = FxDialogs.promptChoice(
                    "Operações Cliente",
                    "Conta: " + conta.getIban(),
                    "Operações disponíveis",
                    options
            );

            if (choiceOpt.isEmpty() || choiceOpt.get().equals("Sair da Conta")) {
                return;
            }

            switch (choiceOpt.get()) {
                case "Consultar Saldo":
                    mostrarSaldo(conta);
                    break;
                case "Depositar dinheiro":
                    depositarDinheiro(conta);
                    break;
                case "Levantar dinheiro":
                    levantarDinheiro(conta);
                    break;
                case "Fazer Transferências":
                    realizarTransferencia(conta);
                    break;
                default:
                    FxDialogs.warning("Operações", "Opção inválida.");
            }
        }
    }

    private void handleAtm() {
        Conta conta = loginAtm();
        if (conta == null) {
            return;
        }

        List<String> options = List.of(
                "Consultar Saldo",
                "Levantar dinheiro",
                "Transferir dinheiro",
                "Últimos Movimentos",
                "Alterar Pin",
                "Voltar"
        );

        while (true) {
            Optional<String> choiceOpt = FxDialogs.promptChoice(
                    "ATM",
                    "Conta: " + conta.getIban(),
                    "Operações disponíveis",
                    options
            );

            if (choiceOpt.isEmpty() || choiceOpt.get().equals("Voltar")) {
                return;
            }

            switch (choiceOpt.get()) {
                case "Consultar Saldo":
                    mostrarSaldo(conta);
                    break;
                case "Levantar dinheiro":
                    levantarDinheiro(conta);
                    break;
                case "Transferir dinheiro":
                    realizarTransferencia(conta);
                    break;
                case "Últimos Movimentos":
                    mostrarMovimentos(conta, false);
                    break;
                case "Alterar Pin":
                    alterarPin(conta);
                    break;
                default:
                    FxDialogs.warning("ATM", "Opção inválida.");
            }
        }
    }

    private Conta loginAtm() {
        int counter = 0;
        String ultimoCartao = "";

        while (counter < 3) {
            Optional<String> cartaoOpt = FxDialogs.promptText("ATM", "Login", "Nº Cartão");
            if (cartaoOpt.isEmpty()) {
                return null;
            }

            String numeroCartao = cartaoOpt.get();
            if (!numeroCartao.equals(ultimoCartao)) {
                counter = 0;
                ultimoCartao = numeroCartao;
            }

            Conta contaExistente = contaRepo.buscarCartaoPorNumero(numeroCartao);
            if (contaExistente == null) {
                FxDialogs.error("ATM", "Cartão não encontrado.");
                return null;
            }
            if (contaExistente.getCartao().isBloqueado()) {
                FxDialogs.error("ATM", "Este cartão encontra-se bloqueado. Contacte o banco.");
                return null;
            }

            Optional<Integer> pinOpt = FxDialogs.promptInteger("ATM", "Login", "Pin");
            if (pinOpt.isEmpty()) {
                return null;
            }

            Conta contaLogada = contaRepo.buscarCartao(numeroCartao, pinOpt.get());

            if (contaLogada != null) {
                FxDialogs.info("ATM", "Login bem-sucedido!");
                return contaLogada;
            }

            counter++;
            FxDialogs.error("ATM", "Nº Cartão ou pin incorretos! Tentativas: " + counter + "/3");

            if (counter >= 3) {
                contaExistente.getCartao().bloquearCartao();
                contaRepo.atualizar(contaExistente);
                FxDialogs.error("ATM", "Limite de tentativas excedido. Cartão BLOQUEADO!");
                return null;
            }
        }

        return null;
    }

    private void handleWebBanking() {
        Cliente cliente = loginWebBanking();
        if (cliente == null) {
            return;
        }

        List<String> options = List.of(
                "Aceder à Conta",
                "Alterar Password",
                "Logout"
        );

        while (true) {
            Optional<String> choiceOpt = FxDialogs.promptChoice(
                    "WebBanking",
                    "Cliente: " + cliente.getNome(),
                    "Operações disponíveis",
                    options
            );

            if (choiceOpt.isEmpty() || choiceOpt.get().equals("Logout")) {
                FxDialogs.info("WebBanking", "Logout efetuado. Até breve!");
                return;
            }

            switch (choiceOpt.get()) {
                case "Aceder à Conta":
                    Conta conta = selecionarConta(contaRepo.buscarContasCliente(cliente.getNif()), "Selecionar Conta");
                    if (conta != null) {
                        handleWebConta(conta);
                    }
                    break;
                case "Alterar Password":
                    alterarPassword(cliente);
                    break;
                default:
                    FxDialogs.warning("WebBanking", "Opção inválida.");
            }
        }
    }

    private Cliente loginWebBanking() {
        int tentativas = 0;

        while (tentativas < 3) {
            Optional<String> userOpt = FxDialogs.promptText("WebBanking", "Autenticação", "Username");
            if (userOpt.isEmpty() || userOpt.get().isEmpty()) {
                FxDialogs.warning("WebBanking", "Login cancelado.");
                return null;
            }

            Optional<String> passOpt = FxDialogs.promptPassword("WebBanking", "Autenticação", "Password");
            if (passOpt.isEmpty()) {
                return null;
            }

            Cliente cliente = clienteRepo.buscarInfoCliente(userOpt.get(), passOpt.get());
            if (cliente != null) {
                FxDialogs.info("WebBanking", "Autenticação bem-sucedida! Bem-vindo(a), " + cliente.getNome());
                return cliente;
            }

            tentativas++;
            FxDialogs.error("WebBanking", "Credenciais inválidas! Tentativa " + tentativas + "/3");
        }

        FxDialogs.error("WebBanking", "Número máximo de tentativas excedido. Acesso bloqueado temporariamente.");
        return null;
    }

    private void handleWebConta(Conta conta) {
        List<String> options = List.of(
                "Consultar Saldo",
                "Fazer Transferências",
                "Ver Movimentos",
                "Sair da Conta"
        );

        while (true) {
            Optional<String> choiceOpt = FxDialogs.promptChoice(
                    "WebBanking - Conta",
                    "Conta: " + conta.getIban(),
                    "Operações disponíveis",
                    options
            );

            if (choiceOpt.isEmpty() || choiceOpt.get().equals("Sair da Conta")) {
                return;
            }

            switch (choiceOpt.get()) {
                case "Consultar Saldo":
                    mostrarSaldo(conta);
                    break;
                case "Fazer Transferências":
                    realizarTransferencia(conta);
                    break;
                case "Ver Movimentos":
                    mostrarMovimentos(conta, true);
                    break;
                default:
                    FxDialogs.warning("WebBanking", "Opção inválida.");
            }
        }
    }

    private void alterarPassword(Cliente cliente) {
        Optional<String> atualOpt = FxDialogs.promptPassword("Alterar Password", "Password atual", "Password atual");
        if (atualOpt.isEmpty()) {
            return;
        }
        if (!atualOpt.get().equals(cliente.getSenha())) {
            FxDialogs.error("Alterar Password", "Password atual incorreta!");
            return;
        }

        Optional<String> novaOpt = FxDialogs.promptPassword("Alterar Password", "Nova password", "Nova password");
        if (novaOpt.isEmpty()) {
            return;
        }
        if (novaOpt.get().length() < 4) {
            FxDialogs.error("Alterar Password", "A password deve ter pelo menos 4 caracteres.");
            return;
        }

        Optional<String> confirmOpt = FxDialogs.promptPassword("Alterar Password", "Confirme a nova password", "Confirmação");
        if (confirmOpt.isEmpty()) {
            return;
        }

        if (!novaOpt.get().equals(confirmOpt.get())) {
            FxDialogs.error("Alterar Password", "As passwords não coincidem!");
            return;
        }

        cliente.setSenha(novaOpt.get());
        clienteRepo.atualizar(cliente);
        FxDialogs.info("Alterar Password", "Password alterada com sucesso!");
    }

    private void mostrarSaldo(Conta conta) {
        String detalhes = String.join("\n",
                "IBAN          : " + conta.getIban(),
                "Tipo de Conta : " + conta.getTipoConta(),
                "NIF Titular   : " + conta.getNifCliente(),
                "SALDO ATUAL   : " + String.format("%.2f EUR", conta.getSaldo())
        );

        FxDialogs.showScrollable("Consultar Saldo", "Saldo", detalhes);
    }

    private void depositarDinheiro(Conta conta) {
        Optional<Double> valorOpt = FxDialogs.promptDouble("Depósito", "Introduza o valor a depositar", "Valor");
        if (valorOpt.isEmpty()) {
            FxDialogs.error("Depósito", "Valor inválido.");
            return;
        }

        double valor = valorOpt.get();
        if (valor <= 0) {
            FxDialogs.error("Depósito", "O valor introduzido é inválido.");
            return;
        }

        conta.depositarDinheiro(valor);
        contaRepo.atualizar(conta);
        FxDialogs.info("Depósito", "Depósito realizado com sucesso! Novo saldo: " + String.format("%.2f EUR", conta.getSaldo()));
    }

    private void levantarDinheiro(Conta conta) {
        Optional<Double> valorOpt = FxDialogs.promptDouble("Levantamento", "Quantidade a levantar", "Valor");
        if (valorOpt.isEmpty()) {
            FxDialogs.error("Levantamento", "Valor inválido.");
            return;
        }

        double valor = valorOpt.get();
        boolean sucesso = conta.levantarDinheiroSemPausa(valor);
        if (!sucesso) {
            FxDialogs.error("Levantamento", "Saldo insuficiente ou valor inválido.");
            return;
        }

        contaRepo.atualizar(conta);
        FxDialogs.info("Levantamento", "Levantamento realizado com sucesso! Novo saldo: " + String.format("%.2f EUR", conta.getSaldo()));
    }

    private void realizarTransferencia(Conta conta) {
        Optional<String> ibanOpt = FxDialogs.promptText("Transferência", "IBAN do destinatário", "IBAN");
        if (ibanOpt.isEmpty()) {
            FxDialogs.warning("Transferência", "Operação cancelada.");
            return;
        }

        String ibanDest = ibanOpt.get().trim();
        if (ibanDest.isEmpty()) {
            FxDialogs.warning("Transferência", "Operação cancelada.");
            return;
        }

        Optional<Double> valorOpt = FxDialogs.promptDouble("Transferência", "Valor a transferir (EUR)", "Valor");
        if (valorOpt.isEmpty()) {
            FxDialogs.error("Transferência", "Valor inválido para transferência.");
            return;
        }

        double valor = valorOpt.get();
        if (valor <= 0) {
            FxDialogs.error("Transferência", "Valor inválido para transferência.");
            return;
        }

        if (valor > conta.getSaldo()) {
            FxDialogs.error("Transferência", "Saldo insuficiente para a transferência.");
            return;
        }

        Conta contaDestinatario = contaRepo.buscarPorIban(ibanDest);
        if (contaDestinatario == null) {
            FxDialogs.error("Transferência", "IBAN do destinatário não encontrado.");
            return;
        }

        if (conta.getIban().equals(ibanDest)) {
            FxDialogs.error("Transferência", "Não pode transferir para a mesma conta.");
            return;
        }

        boolean confirmar = FxDialogs.confirm(
                "Transferência",
                "Confirmar Transferência",
                "De: " + conta.getIban() + "\nPara: " + ibanDest + "\nValor: " + String.format("%.2f EUR", valor)
        );

        if (!confirmar) {
            FxDialogs.warning("Transferência", "Transferência cancelada.");
            return;
        }

        double saldoAnterior = conta.getSaldo();
        conta.adicionarSaldo(-valor);
        contaDestinatario.adicionarSaldo(valor);

        MovimentoCSVRepository movimentoRepo = new MovimentoCSVRepository();
        Movimento movEnvio = new Movimento(new Date(), valor, conta.getSaldo(), TipoMove.enviar, ibanDest);
        conta.getMovimentos().add(movEnvio);
        movimentoRepo.salvar(conta.getIban(), movEnvio);

        Movimento movReceber = new Movimento(new Date(), valor, contaDestinatario.getSaldo(), TipoMove.receber, conta.getIban());
        contaDestinatario.getMovimentos().add(movReceber);
        movimentoRepo.salvar(contaDestinatario.getIban(), movReceber);

        contaRepo.atualizar(conta);
        contaRepo.atualizar(contaDestinatario);

        FxDialogs.info(
                "Transferência",
                "Transferência realizada com sucesso!\nSaldo anterior: " + String.format("%.2f EUR", saldoAnterior)
                        + "\nSaldo atual: " + String.format("%.2f EUR", conta.getSaldo())
        );
    }

    private void mostrarMovimentos(Conta conta, boolean reverso) {
        List<Movimento> movimentos = new ArrayList<>(conta.getMovimentos());
        if (movimentos.isEmpty()) {
            FxDialogs.warning("Movimentos", "Nenhum movimento encontrado.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        if (reverso) {
            for (int i = movimentos.size() - 1; i >= 0; i--) {
                sb.append(movimentos.get(i)).append("\n");
            }
        } else {
            for (Movimento movimento : movimentos) {
                sb.append(movimento).append("\n");
            }
        }

        FxDialogs.showScrollable("Movimentos", "Histórico de Movimentos", sb.toString());
    }

    private void alterarPin(Conta conta) {
        Optional<Integer> pinOpt = FxDialogs.promptInteger("Alterar Pin", "Novo pin", "Pin");
        if (pinOpt.isEmpty()) {
            FxDialogs.error("Alterar Pin", "Pin inválido.");
            return;
        }

        conta.getCartao().setPin(pinOpt.get());
        contaRepo.atualizar(conta);
        FxDialogs.info("Alterar Pin", "Pin atualizado com sucesso.");
    }

    private Conta selecionarConta(List<Conta> contas, String titulo) {
        if (contas == null || contas.isEmpty()) {
            FxDialogs.warning(titulo, "Não possui contas associadas.");
            return null;
        }

        List<String> options = new ArrayList<>();
        for (Conta conta : contas) {
            options.add(conta.getTipoConta() + " - IBAN: " + conta.getIban());
        }

        Optional<String> choiceOpt = FxDialogs.promptChoice(titulo, "Selecionar Conta", "Contas disponíveis", options);
        if (choiceOpt.isEmpty()) {
            return null;
        }

        int index = options.indexOf(choiceOpt.get());
        if (index < 0) {
            return null;
        }

        return contas.get(index);
    }
}
