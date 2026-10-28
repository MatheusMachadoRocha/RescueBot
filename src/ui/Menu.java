
package ui;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import model.Alimento;
import model.Endereco;
import model.Familia;
import model.Higiene;
import model.Item;
import model.ItemSolicitado;
import model.Solicitacao;
import model.Vestuario;
import service.Sistema;

public class Menu {

    private final Sistema sistema;
    private final Scanner scanner;

    public Menu(Sistema sistema, Scanner scanner) {
        this.sistema = sistema;
        this.scanner = scanner;
    }

    public void executar() {
        boolean ativo = true;

        while (ativo) {
            try {
                imprimirMenu();
                int opcao = lerInteiro("Escolha: ");

                switch (opcao) {
                    case 1 -> registrarDoacao();
                    case 2 -> listarEstoque();
                    case 3 -> procurarProduto();
                    case 4 -> removerProduto();
                    case 5 -> registrarFamilia();
                    case 6 -> listarFamilias();
                    case 7 -> criarSolicitacao();
                    case 8 -> atenderSolicitacao();
                    case 9 -> listarVencimento();
                    case 10 -> listarEstoqueBaixo();
                    case 11 -> System.out.println(sistema.getEstoque().mostrarMapa());
                    case 12 -> System.out.println(sistema.gerarRelatorio());
                    case 0 -> ativo = false;
                    default -> System.out.println("Opção inválida.");
                }

            } catch (NoSuchElementException e) {
                ativo = false;

            } catch (IllegalArgumentException | IllegalStateException |
                     IllegalAccessException e) {
                System.out.println("Erro: " + e.getMessage());
            }

            System.out.println();
        }

        System.out.println("Sistema encerrado.");
    }

    private void imprimirMenu() {
        System.out.println("""
                
                === RESCUESTOCK ===
                1  - Registrar doação
                2  - Listar estoque
                3  - Procurar produto
                4  - Remover produto
                5  - Registrar família
                6  - Listar famílias
                7  - Criar solicitação
                8  - Atender solicitação
                9  - Produtos próximos do vencimento
                10 - Produtos com estoque baixo
                11 - Mapa do depósito
                12 - Relatório geral
                0  - Sair
                """);
    }

    private void registrarDoacao() throws IllegalAccessException {
        String nome = lerTexto("Nome: ");
        int quantidade = lerInteiroPositivo("Quantidade: ");
        double peso = lerPeso();

        int tipo = lerInteiro("Tipo (1-Alimento, 2-Higiene, 3-Vestuário): ");
        Item item;

        if (tipo == 1) {
            int dias = lerInteiroNaoNegativo("Dias para vencer: ");
            item = new Alimento(nome, quantidade, peso, dias);

        } else if (tipo == 2) {
            boolean infantil = lerSimNao("Uso infantil (s/n): ");
            item = new Higiene(nome, quantidade, peso, infantil);

        } else if (tipo == 3) {
            String tamanho = lerTexto("Tamanho: ");
            item = new Vestuario(nome, quantidade, peso, tamanho);

        } else {
            throw new IllegalArgumentException("Tipo inválido.");
        }

        sistema.getEstoque().cadastrarDoacoes(item);
        System.out.println("Doação registrada.");
    }

    private void listarEstoque() {
        ArrayList<Item> itens = sistema.getEstoque().getItens();

        if (itens.isEmpty()) {
            System.out.println("Estoque vazio.");
        }

        for (Item item : itens) {
            System.out.println(item + " | Posição: "
                    + sistema.getEstoque().getPosicao(item.getNome()));
        }
    }

    private void procurarProduto() {
        String nome = lerTexto("Produto ou trecho: ");
        ArrayList<Item> encontrados = sistema.getEstoque().pesquisar(nome);

        if (encontrados.isEmpty()) {
            System.out.println("Produto não encontrado.");
        }

        for (Item item : encontrados) {
            System.out.println(item + " | Posição: "
                    + sistema.getEstoque().getPosicao(item.getNome()));
        }
    }

    private void removerProduto() {
        String nome = lerTexto("Produto: ");
        int opcao = lerInteiro("1 - Retirar quantidade | 2 - Remover cadastro: ");

        boolean alterou = false;

        if (opcao == 1) {
            int quantidade = lerInteiroPositivo("Quantidade: ");
            alterou = sistema.getEstoque().retirarQuantidade(nome, quantidade);

        } else if (opcao == 2) {
            alterou = sistema.getEstoque().removerProduto(nome);
        }

        if (alterou) {
            System.out.println("Estoque atualizado.");
        } else {
            System.out.println("Produto não encontrado ou quantidade insuficiente.");
        }
    }

    private void registrarFamilia() {
        String responsavel = lerTexto("Responsável: ");
        int adultos = lerInteiroNaoNegativo("Adultos: ");
        int criancas = lerInteiroNaoNegativo("Crianças: ");

        Endereco endereco = new Endereco(
                lerTexto("Rua: "),
                lerTexto("Cidade: "),
                lerTexto("Bairro: ")
        );

        Familia familia = sistema.cadastrarFamili(
                responsavel, adultos, criancas, endereco
        );

        System.out.println("Família cadastrada: " + familia);
    }

    private void listarFamilias() {
        ArrayList<Familia> familias = sistema.getFamiliasPorPrioridade();

        if (familias.isEmpty()) {
            System.out.println("Nenhuma família cadastrada.");
        }

        for (Familia familia : familias) {
            System.out.println(familia);
        }
    }

    private void criarSolicitacao() {
        int familia = lerInteiroPositivo("ID da família: ");
        int total = lerInteiroPositivo("Quantos tipos de produto? ");

        ArrayList<ItemSolicitado> pedidos = new ArrayList<>();

        for (int i = 1; i <= total; i++) {
            String produto = lerTexto("Produto " + i + ": ");
            int quantidade = lerInteiroPositivo("Quantidade: ");

            pedidos.add(new ItemSolicitado(produto, quantidade));
        }

        Solicitacao solicitacao = sistema.criarSolicitacao(
                familia, pedidos.toArray(new ItemSolicitado[0])
        );

        System.out.println("Solicitação criada: " + solicitacao);
    }

    private void atenderSolicitacao() {
        ArrayList<Solicitacao> pendentes = sistema.getSolicitacoesPendentes();

        if (pendentes.isEmpty()) {
            System.out.println("Não há solicitações pendentes.");
            return;
        }

        for (Solicitacao solicitacao : pendentes) {
            System.out.println(solicitacao + " | Prioridade: "
                    + solicitacao.getFamilia().calcularPrioridade());
        }

        int id = lerInteiroPositivo("ID da solicitação: ");
        ArrayList<String> faltas = sistema.verificarSolicitacao(id);

        if (!faltas.isEmpty()) {
            System.out.println("Itens insuficientes:");

            for (String falta : faltas) {
                System.out.println(falta);
            }

            return;
        }

        boolean atendida = sistema.atenderSolicitacao(id);

        if (atendida) {
            System.out.println("Solicitação atendida. Estoque atualizado.");
        } else {
            System.out.println("Não foi possível atender a solicitação.");
        }
    }

    private void listarVencimento() {
        int dias = lerInteiroNaoNegativo("Vencimento em até quantos dias? ");

        ArrayList<Alimento> alimentos =
                sistema.getEstoque().getProdutosProximosVencimentos(dias);

        if (alimentos.isEmpty()) {
            System.out.println("Nenhum alimento encontrado.");
        }

        for (Alimento alimento : alimentos) {
            System.out.println(
                    alimento.getNome() + " - "
                            + alimento.getDiasParaVencer() + " dias - "
                            + alimento.getQuantidade() + " unidades"
            );
        }
    }

    private void listarEstoqueBaixo() {
        int limite = lerInteiroNaoNegativo("Limite de estoque: ");

        ArrayList<Item> itens =
                sistema.getEstoque().getProdutosEstoqueBaixo(limite);

        if (itens.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
        }

        for (Item item : itens) {
            System.out.println(
                    item.getNome() + " - "
                            + item.getQuantidade() + " unidades"
            );
        }
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        String texto = scanner.nextLine().trim();

        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Campo obrigatório.");
        }

        return texto;
    }

    private int lerInteiro(String mensagem) {
        System.out.print(mensagem);

        try {
            return Integer.parseInt(scanner.nextLine().trim());

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Digite um número inteiro válido.");
        }
    }

    private int lerInteiroPositivo(String mensagem) {
        int valor = lerInteiro(mensagem);

        if (valor <= 0) {
            throw new IllegalArgumentException("Digite um número maior que zero.");
        }

        return valor;
    }

    private int lerInteiroNaoNegativo(String mensagem) {
        int valor = lerInteiro(mensagem);

        if (valor < 0) {
            throw new IllegalArgumentException("Digite zero ou um número positivo.");
        }

        return valor;
    }

    private double lerPeso() {
        System.out.print("Peso unitário (kg): ");

        try {
            String entrada = scanner.nextLine().trim().replace(',', '.');
            double peso = Double.parseDouble(entrada);

            if (peso <= 0 || Double.isNaN(peso) || Double.isInfinite(peso)) {
                throw new NumberFormatException();
            }

            return peso;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Digite um peso positivo válido.");
        }
    }

    private boolean lerSimNao(String mensagem) {
        String resposta = lerTexto(mensagem);

        if (resposta.equalsIgnoreCase("s")) {
            return true;
        }

        if (resposta.equalsIgnoreCase("n")) {
            return false;
        }

        throw new IllegalArgumentException("Digite s ou n.");
    }
}
