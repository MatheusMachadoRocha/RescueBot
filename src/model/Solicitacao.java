package model;

import java.util.ArrayList;

public class Solicitacao {
    private final int id;
    private final Familia familia;
    private final ArrayList<ItemSolicitado> itens = new ArrayList<>();
    private boolean atendida;

    public Solicitacao(int id, Familia familia,ItemSolicitado... pedidos) {
        if(id <= 0 || familia == null || pedidos == null || pedidos.length == 0){
            throw new IllegalArgumentException("Soliciacao Invalida");
        }
        this.id = id;
        this.familia = familia;
        for(ItemSolicitado pedido :pedidos){
            adicionarOuSomar(pedido);
        }
    }

    private void adicionarOuSomar(ItemSolicitado pedido){
        if(pedido == null) {
            throw new IllegalArgumentException("Pedido precisa ter itens");
        }

        for(int i=0;i < itens.size();i++){
            if(itens.get(i).nome().equalsIgnoreCase(pedido.nome())){
                int soma = itens.get(i).quantidade() + pedido.quantidade();
                itens.set(i,new ItemSolicitado(itens.get(i).nome(),soma));
            }
        }
        itens.add(pedido);
    }

    public int getId() {
        return id;
    }

    public Familia getFamilia() {
        return familia;
    }

    public ArrayList<ItemSolicitado> getItens() {
        return itens;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public void marcarAtendida(){
        if (atendida){
            System.out.println("A soliciacao ja foi atendida");
        } else {
            atendida = true;
        }
    }

    @Override
    public String toString() {
        return "Solicitacao{" +
                "id=" + id +
                ", familia=" + familia +
                ", itens=" + itens +
                ", atendida=" + atendida +
                '}';
    }
}
