package br.sc.weg.sid.model.enums;

public enum Cargo {
    ANALISTA("Analista"),
    SOLICITANTE("Solicitante"),
    GESTOR_TI("Gestor TI"),
    GERENTE("Gerente");

    String nome;
    Cargo(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
