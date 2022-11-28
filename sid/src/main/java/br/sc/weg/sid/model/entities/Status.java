package br.sc.weg.sid.model.entities;

public enum Status {
    ASSESSMENT("Assesment"),
    BACKLOG("Backlog"),
    TODO("To-do"),
    DESIGN_AND_BUILD("Design and Build"),
    BUSINESS_CASE("Business Case"),
    SUPPORT("Support"),
    DONE("Done"),
    CANCELLED("Cancelled"),
    RASCUNHO("Rascunho");

    String nome;
    Status(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
