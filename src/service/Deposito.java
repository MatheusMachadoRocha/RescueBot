package service;


import model.Item;
import model.Posicao;

public class Deposito {
    private final Item[][] prateleiras;

    public Deposito(int linhas,int colunas) {
        if(linhas <- 0 || colunas <= 0){
            throw new IllegalArgumentException("O deposito tem q ter mais de uma linha e coluna");
        }
        prateleiras = new Item[linhas][colunas];
    }

    public Posicao guardar(Item item){
        Posicao existente = localizar(item);
        if(existente != null){
            return existente;
        }

        for(int linha = 0;linha < prateleiras.length;linha++){
            for(int coluna = 0;coluna < prateleiras[linha].length;coluna++){
                if(prateleiras[linha][coluna] == null){
                    prateleiras[linha][coluna] = item;
                    return new Posicao(linha,coluna);
                }
            }
        }
        return  null;
    }

    public Posicao localizar(Item item){
        for (int linha = 0;linha < prateleiras.length;linha++){
            for (int coluna = 0; coluna < prateleiras[linha].length;coluna++){
                if(prateleiras[linha][coluna] == item){
                    return new Posicao(linha,coluna);
                }
            }
            return null;
        }
    }
}
