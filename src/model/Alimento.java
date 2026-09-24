package model;

public class Alimento extends Item{
    private final int diasParaVencer;


    public Alimento(String nome, int quantidade, double peso, int diasParaVencer) throws IllegalAccessException {
        super(nome, quantidade, peso);

        if(diasParaVencer < 0){
            throw new IllegalAccessException("Os dias precisam ser maior que 0");
        }

        this.diasParaVencer = diasParaVencer;
    }

    public int getDiasParaVencer() {
        return diasParaVencer;
    }

    @Override
    public String getTipo() {
        return "Alimento";
    }

    @Override
    public String getPrioridadeDistribuicao() {
        return diasParaVencer <= 2 ? "URGENTE" : diasParaVencer <= 7 ? "ALTA" : "NORMAL";
    }

    @Override
    public boolean compativelCom(Item outro) {
        return super.compativelCom(outro) && diasParaVencer == ((Alimento) outro).diasParaVencer;
    }

    @Override
    public String toString() {
        return super.toString() + ", dias para vencer=" + diasParaVencer;
    }
}
