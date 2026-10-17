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

    public boolean retirar(Item item){
        Posicao posicao = localizar(item);
        if(posicao == null) return false;
        prateleiras[posicao.linha()][posicao.coluna()] = null;
        return true;
    }

    public Posicao localizar(Item item){
        for (int linha = 0;linha < prateleiras.length;linha++){
            for (int coluna = 0; coluna < prateleiras[linha].length;coluna++){
                if(prateleiras[linha][coluna] == item){
                    return new Posicao(linha,coluna);
                }
            }

        }
        return null;
    }

    public int getCapacidade(){
        return prateleiras.length * prateleiras[0].length;
    }

    public int getOcupacao(){
        int ocupacao = 0;
        for(Item[] linha : prateleiras){
            for(Item item: linha) if (item != null) ocupacao++;
        }
        return ocupacao;
    }

    public String mostrarMapa(){
        StringBuilder mapa = new StringBuilder("DEPÓSITO\n\n");
        for(Item[] linha : prateleiras){
            for(Item item : linha) mapa.append(item == null ? "[ ]" : "[x]");
            mapa.append('\n');
        }
        int percentual = getOcupacao() * 100 /getCapacidade();
        mapa.append("Ocupação: ").append(percentual).append("% (")
                .append(getOcupacao()).append('/').append(getCapacidade()).append(")");
        return mapa.toString();
    }
}
