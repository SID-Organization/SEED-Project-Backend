package br.sc.weg.sid.model.entities;

public enum Tamanho {
    MUITO_PEQUENA("Muito Pequena"),
    PEQUENA("Pequena"),
    MEDIA("Média"),
    GRANDE("Grande"),
    MUITO_GRANDE("Muito Grande");

    String nome;
    Tamanho(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
