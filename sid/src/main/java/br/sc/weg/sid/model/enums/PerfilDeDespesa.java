package br.sc.weg.sid.model.enums;

public enum PerfilDeDespesa {
    TIPO1("Tipo 1"),
    TIPO2("Tipo 2"),
    TIPO3("Tipo 3");

    String nome;
    PerfilDeDespesa(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
