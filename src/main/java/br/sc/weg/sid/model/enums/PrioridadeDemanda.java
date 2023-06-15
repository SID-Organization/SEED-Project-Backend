package br.sc.weg.sid.model.enums;

import lombok.Getter;

@Getter
public enum PrioridadeDemanda {

    TRIVIAl("Trivial", 1),

    MINOR("Minor", 2),

    MAJOR("Major", 4),

    CRITICAL("Critical", 16),

    BLOCKER("Blocker", 100000);

    String nome;
    Integer valor;

    PrioridadeDemanda(String nome, Integer valor){
        this.nome = nome;
        this.valor = valor;
    }

}
