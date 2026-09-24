package model;

public class Higiene extends Item{
    private final boolean usoInfantil;


    public Higiene(String nome, int quantidade, double peso,boolean usoInfantil) throws IllegalAccessException {
        super(nome, quantidade, peso);
        this.usoInfantil = usoInfantil;
    }

    public boolean isUsoInfantil() {
        return usoInfantil;
    }

    @Override
    public String getTipo() {
        return "Higiene";
    }

    @Override
    public boolean compativelCom(Item outro) {
        return super.compativelCom(outro) && usoInfantil == ((Higiene) outro).usoInfantil;
    }

    @Override
    public String toString() {
        return super.toString() + ", Uso infantil?=" + usoInfantil;
    }
}
