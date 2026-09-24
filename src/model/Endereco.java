package model;

public record Endereco(String rua, String cidade,String bairro) {
    public Endereco {
        if (rua == null || cidade == null || bairro == null) {
            throw new IllegalArgumentException("Os valores sao obrigatorios");
        }
    }
}
