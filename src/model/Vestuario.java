package model;

public class Vestuario extends Item{
    private final String tamanho;
    public Vestuario(String nome, int quantidade, double peso,String tamanho) throws IllegalAccessException {
        super(nome,quantidade,peso);
        if(tamanho == null || tamanho.trim().isEmpty()) throw new IllegalArgumentException("Informe o tamanho");
        this.tamanho = tamanho.trim();
    }

    public String getTamanho(){
        return tamanho;
    }

    @Override
    public String getTipo() {
        return "Vestuario";
    }

    @Override
    public boolean compativelCom(Item outro) {
        return super.compativelCom(outro) && tamanho.equalsIgnoreCase(((Vestuario) outro).tamanho);
    }

    @Override
    public String toString() {
        return super.toString() + "Tamanho" + tamanho;
    }
}
