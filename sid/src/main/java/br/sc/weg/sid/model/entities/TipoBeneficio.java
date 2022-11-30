package br.sc.weg.sid.model.entities;

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
