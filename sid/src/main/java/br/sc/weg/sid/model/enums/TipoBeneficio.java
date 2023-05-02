package br.sc.weg.sid.model.enums;

public enum TipoBeneficio {
    QUALITATIVO("Qualitativo"),
    REAL("Real"),
    POTENCIAL("Potencial");


    String nome;

    TipoBeneficio(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

}
