package service;

import model.Item;
import model.Alimento;
import model.Posicao;


import java.util.ArrayList;
import java.util.Locale;

public class Estoque {
    private final ArrayList<Item> itens = new ArrayList<>();
    private final Deposito deposito;

    public Estoque(){
        this(4,4);
    }

    public Estoque(int linhas, int colunas){
        deposito = new Deposito(linhas,colunas);
    }

    public void cadastrarDoacoes(Item... novasDoacoes){
        if(novasDoacoes == null || novasDoacoes.length == 0){
            throw new IllegalArgumentException("Informe ao menos uma doação");
        }

        for(Item novo : novasDoacoes){
            if(novo == null || novo.getQuantidade() <= 0){
                throw new IllegalArgumentException("Toda doacao precisa ter quantidade positiva");
            }
        }
        for(Item novo : novasDoacoes) cadastrarDoacoes(novo);
    }

    public Item buscar(String nome){
        if(nome == null) return null;
        for(Item item : itens) if (item.getNome().equalsIgnoreCase(nome.trim())) return item;
        return null;
    }

    private void cadastrarDoacao(Item novo) throws IllegalAccessException {
        Item existente = buscar(novo.getNome());
        if(existente != null){
            if(!existente.compativelCom(novo)){
                throw new IllegalArgumentException("Ja existe '" + existente.getNome() + "' com caracteristicas diferente. Use outro nome para separar o lote.");
            }
            existente.adicionarQuantidade(novo.getQuantidade());
            return;
        }
        if(deposito.guardar(novo) == null){
            throw new IllegalStateException("O deposito esta cheio. remova um produto antes de cadastrar um novo");
        }
        itens.add(novo);
    }

    public ArrayList<Item> getItens(){
        return new ArrayList<>(itens);
    }

    public ArrayList<Item> pesquisar(String trecho){
        ArrayList<Item> encontrados = new ArrayList<>();
        if(trecho == null) return encontrados;
        String termo = trecho.trim().toLowerCase();
        for(Item item : itens){
            if(item.getNome().toLowerCase().contains(termo)) encontrados.add(item);
        }
        return encontrados;
    }

    public boolean removerProduto(String nome){
        Item item = buscar(nome);
        if(item == null) return false;
        deposito.retirar(item);
        itens.remove(item);
        return true;
    }

    public boolean retirarQuantidade(String nome,int quantidade){
        Item item = buscar(nome);
        if(item == null || !item.retirarQuantidade(quantidade)) return false;
        if(item.getQuantidade() == 0){
            deposito.retirar(item);
            itens.remove(item);
        }
        return true;
    }

    public Posicao getPosicao(String nome){
        Item item = buscar(nome);
        return item == null ? null : deposito.localizar(item);
    }

    public ArrayList<Alimento> getProdutosProximosVencimentos(int dias){
        if(dias < 0) throw new IllegalArgumentException("Os dias nao podem ser negativos");
        ArrayList<Alimento> alimentos = new ArrayList<>();
        for(Item item : itens) if(item instanceof Alimento alimento && alimento.getDiasParaVencer() <= dias) alimentos.add(alimento);
        ordenarAlimentosPorValidade(alimentos);
        return alimentos;
    }

    public void ordenarAlimentosPorValidade(ArrayList<Alimento> alimentos){
        for(int i = 0; i < alimentos.size() - 1;i++){
            for(int j = 0; i < alimentos.size() - 1;i++){
                if(alimentos.get(j).getDiasParaVencer() > alimentos.get(j + 1).getDiasParaVencer()){
                    Alimento auxiliar = alimentos.get(j);
                    alimentos.set(j,alimentos.get(j + 1));
                    alimentos.set(j + 1, auxiliar);
                }
            }
        }
    }

    public ArrayList<Item> getProdutosEstoqueBaixo(int limite){
        if(limite < 0) throw new IllegalArgumentException(("O limiet nao pode ser negativo"));
        ArrayList<Item> baixos = new ArrayList<>();
        for(Item item : itens) if(item.getQuantidade() <= limite) baixos.add(item);{
            return baixos;
        }
    }

    public ArrayList<Item> getItensPorPropriedade(){
        ArrayList<Item> ordenados = new ArrayList<>(itens);
        for(int i = 0; i < ordenados.size() - 1;i++){
            for(int j = 0;j < ordenados.size() -1 - i;j++){
                if(nivel(ordenados.get(j).getPrioridadeDistribuicao()) < nivel(ordenados.get(j + 1).getPrioridadeDistribuicao())){
                    Item auxiliar = ordenados.get(j);
                    ordenados.set(j,ordenados.get(j + 1));
                    ordenados.set(j + 1,auxiliar);
                }

            }
        }
        return ordenados;
    }

    private int nivel(String prioridade){
            if(prioridade.equals("URGENTE")) return 3;
            if(prioridade.equals("ALTA")) return 2;
            return 1;
    }

    public String mostrarMapa(){
        return deposito.mostrarMapa();
    }

    public int getCapacidadeDeposito() {
        return deposito.getCapacidade();
    }

    public  int getOcupacaoDeposito(){
        return deposito.getOcupacao();
    }
}
