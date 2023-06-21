package br.sc.weg.sid.model.enums;

public enum ImportanciaDemanda {

    TRIVIAL("Trivial"),
    MINOR("Minor"),
    MAJOR("Major"),
    CRITICAL("Critical"),
    BLOCKER("Blocker");

    String nome;
    ImportanciaDemanda(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

}
