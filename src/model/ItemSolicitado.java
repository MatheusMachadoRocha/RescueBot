package model;

public record ItemSolicitado(String nome,int quantidade) {
    public ItemSolicitado {
        if(nome == null || quantidade <= 0){
            throw new IllegalArgumentException("Item solicitado invalido");
        }
    }
}
