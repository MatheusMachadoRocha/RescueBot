package model;

public record Posicao(int linha,int coluna) {
    public Posicao {
        if(linha < 0 || coluna < 0){
            throw new IllegalArgumentException("A posicao nao e valida");
        }
    }

    @Override
    public String toString() {
        return "[" + linha + "][" + coluna + "]";
    }
}
