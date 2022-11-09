package br.sc.weg.sid.model.entities;

public enum Secao {
    TI("TI"),
    FINANCEIRO("Financeiro"),
    RH("RH"),
    COMERCIAL("Comercial"),
    PRODUCAO("Produção"),
    QUALIDADE("Qualidade"),
    LOGISTICA("Logística"),
    ADMINISTRATIVO("Administrativo"),
    OUTROS("Outros");

    String nome;
    Secao(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
