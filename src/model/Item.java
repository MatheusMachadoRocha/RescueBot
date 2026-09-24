package model;

public class Item {
    private final String nome;
    private int quantidade;
    private final double peso;

    public Item(String nome, int quantidade, double peso) throws IllegalAccessException {
        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalAccessException("Informe o nome do produto");
        }

        if (quantidade < 0) {
            throw new IllegalAccessException("A quantidade não pode ser negativa");
        }

        if (peso <= 0 || Double.isNaN(peso)){
            throw new IllegalAccessException("O peso deve ser maior que zero");
        }
        this.nome = nome.trim();
        this.quantidade = quantidade;
        this.peso = peso;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPeso() {
        return peso;
    }

    public String getTipo() {
        return "Item";
    }

    public String getPrioridadeDistribuicao() {
        return "NORMAL";
    }

    public void adicionarQuantidade(int valor) throws IllegalAccessException {
        if(valor <= 0){
            throw new IllegalAccessException();
        }
        try{
            quantidade += valor;
        } catch (IllegalAccessError e){
            System.out.println("Quantidade Invalida");
        }
    }

    public boolean retirarQuantidade(int valor) {
        if(valor <= 0 || valor > quantidade){
            return false;
        } else {
            quantidade -= valor;
            return true;
        }
    }

    public boolean  compativelCom(Item outro){
        return outro != null && getClass() == outro.getClass() && nome.equalsIgnoreCase(outro.nome) && Double.compare(peso, outro.peso) == 0;
    }

    @Override
    public String toString() {
        return "Item{" +
                "nome='" + nome + '\'' +
                ", tipo=" + getTipo() + '\'' +
                ", quantidade=" + quantidade +
                ", peso em kg=" + peso +
                ", prioridade=" + getPrioridadeDistribuicao() +
                '}';
    }
}
