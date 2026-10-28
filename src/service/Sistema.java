package service;

import model.*;

import java.util.ArrayList;

public class Sistema {
    private final Estoque estoque;
    private final ArrayList<Familia> familias = new ArrayList<>();
    private final ArrayList<Solicitacao> solicitacaos = new ArrayList<>();
    private int proximoIdFamilia = 1;
    private int proximoIdSolicitacao = 1;

    public Sistema(){
        estoque = new Estoque();
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public Familia cadastrarFamili(String responsavel, int adultos, int criancas, Endereco endereco){
        Familia familia = new Familia(proximoIdFamilia,responsavel,adultos,criancas,endereco);
        familias.add(familia);
        proximoIdFamilia++;
        return familia;
    }

    public Familia buscarFamilia(int id){
        for(Familia familia : familias) if (familia.getId() == id) return familia;
        return null;
    }

    public ArrayList<Familia> getFamiliasPorPrioridade(){
        ArrayList<Familia> ordenadas = new ArrayList<>(familias);
        for(int i = 0; i < ordenadas.size() - 1;i++){
            for(int j = 0;j < ordenadas.size() -1  - i;j++){
                if(ordenadas.get(j).calcularPrioridade() < ordenadas.get(j+1).calcularPrioridade()){
                    Familia auxiliar = ordenadas.get(j);
                    ordenadas.set(j,ordenadas.get(j+1));
                    ordenadas.set(j+1,auxiliar);
                }
            }
        }
        return ordenadas;
    }

    public Solicitacao criarSolicitacao(int idFamilia, ItemSolicitado... itens) {
        Familia familia = buscarFamilia(idFamilia);
        if (familia == null) throw new IllegalArgumentException("Família não encontrada.");
        Solicitacao solicitacao = new Solicitacao(proximoIdSolicitacao, familia, itens);
        solicitacaos.add(solicitacao);
        proximoIdSolicitacao++;
        return solicitacao;
    }

    public Solicitacao buscarSolicitacao(int id) {
        for (Solicitacao solicitacao : solicitacaos) if (solicitacao.getId() == id) return solicitacao;
        return null;
    }

    public ArrayList<Solicitacao> getSolicitacoesPendentes() {
        ArrayList<Solicitacao> pendentes = new ArrayList<>();
        for (Solicitacao solicitacao : solicitacaos) if (!solicitacao.isAtendida()) pendentes.add(solicitacao);
        for (int i = 0; i < pendentes.size() - 1; i++) {
            for (int j = 0; j < pendentes.size() - 1 - i; j++) {
                if (pendentes.get(j).getFamilia().calcularPrioridade()
                        < pendentes.get(j + 1).getFamilia().calcularPrioridade()) {
                    Solicitacao auxiliar = pendentes.get(j);
                    pendentes.set(j, pendentes.get(j + 1));
                    pendentes.set(j + 1, auxiliar);
                }
            }
        }
        return pendentes;
    }


    public ArrayList<String> verificarSolicitacao(int id) {
        Solicitacao solicitacao = buscarSolicitacao(id);

        if (solicitacao == null) {
            throw new IllegalArgumentException("Solicitação não encontrada.");
        }

        ArrayList<String> faltas = new ArrayList<>();

        for (ItemSolicitado pedido : solicitacao.getItens()) {
            Item disponivel = estoque.buscar(pedido.nome());

            int quantidadeDisponivel = disponivel == null
                    ? 0
                    : disponivel.getQuantidade();

            if (quantidadeDisponivel < pedido.quantidade()) {
                int quantidadeFaltante = pedido.quantidade() - quantidadeDisponivel;

                faltas.add(
                        pedido.nome()
                                + " | Solicitado: " + pedido.quantidade()
                                + " | Disponível: " + quantidadeDisponivel
                                + " | Faltante: " + quantidadeFaltante
                );
            }
        }

        return faltas;
    }

    public boolean atenderSolicitacao(int id) {
        Solicitacao solicitacao = buscarSolicitacao(id);

        if (solicitacao == null) {
            return false;
        }

        if (solicitacao.isAtendida()) {
            return false;
        }

        if (!verificarSolicitacao(id).isEmpty()) {
            return false;
        }

        for (ItemSolicitado pedido : solicitacao.getItens()) {
            estoque.retirarQuantidade(pedido.nome(), pedido.quantidade());
        }

        solicitacao.marcarAtendida();
        return true;
    }

    public String gerarRelatorio() {
        StringBuilder relatorio = new StringBuilder();

        relatorio.append("\n=== RELATÓRIO RESCUESTOCK ===\n");
        relatorio.append("Itens cadastrados: ")
                .append(estoque.getItens().size()).append("\n");

        relatorio.append("Total em estoque: ")
                .append(calcularTotalEstoque()).append(" unidades\n");

        relatorio.append("\n--- ESTOQUE BAIXO ---\n");

        ArrayList<Item> baixos = estoque.getProdutosEstoqueBaixo(5);

        if (baixos.isEmpty()) {
            relatorio.append("Nenhum produto com estoque baixo.\n");
        }

        for (Item item : baixos) {
            relatorio.append(formatarItem(item)).append("\n");
        }

        relatorio.append("\n--- PRÓXIMOS DO VENCIMENTO ---\n");

        ArrayList<Alimento> urgentes =
                estoque.getProdutosProximosVencimentos(2);

        if (urgentes.isEmpty()) {
            relatorio.append("Nenhum alimento próximo do vencimento.\n");
        }

        for (Alimento alimento : urgentes) {
            relatorio.append(alimento.getNome())
                    .append(" - ")
                    .append(alimento.getDiasParaVencer())
                    .append(" dias - ")
                    .append(alimento.getQuantidade())
                    .append(" unidades\n");
        }

        int atendidas = 0;

        for (Solicitacao solicitacao : solicitacaos) {
            if (solicitacao.isAtendida()) {
                atendidas++;
            }
        }

        relatorio.append("\n--- FAMÍLIAS E SOLICITAÇÕES ---\n");
        relatorio.append("Famílias cadastradas: ")
                .append(familias.size()).append("\n");

        relatorio.append("Solicitações pendentes: ")
                .append(getSolicitacoesPendentes().size()).append("\n");

        relatorio.append("Solicitações atendidas: ")
                .append(atendidas).append("\n");

        relatorio.append("\n--- MAPA DO DEPÓSITO ---\n");
        relatorio.append(estoque.mostrarMapa()).append("\n");

        return relatorio.toString();
    }

    private int calcularTotalEstoque() {
        int total = 0;

        for (Item item : estoque.getItens()) {
            total += item.getQuantidade();
        }

        return total;
    }

    private String formatarItem(Item item) {
        return String.format(
                "%-20s %d unidades",
                item.getNome(),
                item.getQuantidade()
        );
    }
}


