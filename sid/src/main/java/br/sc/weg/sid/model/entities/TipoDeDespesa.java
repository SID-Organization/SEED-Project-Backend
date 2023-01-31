package br.sc.weg.sid.model.entities;

public enum TipoDeDespesa {
    INTERNA("Interna"),
    EXTERNA("Externa");

    String nome;
    TipoDeDespesa(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
