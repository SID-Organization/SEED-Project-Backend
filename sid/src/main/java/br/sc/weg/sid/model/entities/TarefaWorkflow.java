package br.sc.weg.sid.model.entities;

public enum TarefaWorkflow {
    PREENCHER_DEMANDA("Preencher Demanda"),
    CLASSIFICACAO_APROVACAO("Classificação e Aprovação"),
    APROVACAO_GERENTE_AREA("Aprovação Gerente da Área"),
    ELABORACAO_PROPOSTA("Elaboração da Proposta");

    String nome;

    TarefaWorkflow(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
