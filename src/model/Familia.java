package model;

public class Familia {
    private final int id;
    private final String responsavel;
    private final int adultos;
    private final int criancas;
    private final Endereco endereco;

    public Familia(int id, String responsavel, int adultos, int criancas, Endereco endereco) {
        if(id <= 0 || responsavel == null || responsavel.trim().isEmpty() || adultos < 0 || criancas < 0 || adultos + criancas == 0 || endereco == null){
            throw new IllegalArgumentException("Dados da familia invalidos");
        }
        this.id = id;
        this.responsavel = responsavel;
        this.adultos = adultos;
        this.criancas = criancas;
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public int getAdultos() {
        return adultos;
    }

    public int getCriancas() {
        return criancas;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public int getTotalPessoa(){
        return  adultos + criancas;
    }

    public int calcularPrioridade(){
        return adultos + 2 * criancas;
    }

    @Override
    public String toString() {
        return "Familia{" +
                "id=" + id +
                ", responsavel='" + responsavel + '\'' +
                ", adultos=" + adultos +
                ", criancas=" + criancas +
                ", endereco=" + endereco +
                '}';
    }
}
