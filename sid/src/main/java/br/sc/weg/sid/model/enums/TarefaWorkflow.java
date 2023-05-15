package br.sc.weg.sid.model.enums;

public enum TarefaWorkflow {
    PREENCHER_DEMANDA("Preencher Demanda"),
    CLASSIFICACAO_APROVACAO("Classificação e Aprovação"),
    APROVACAO_GERENTE_AREA("Aprovação Gerente da Área"),
    ELABORACAO_PROPOSTA("Elaboração da Proposta"),
    PROPOSTA_PRONTA("Proposta Pronta"),
    EM_PAUTA("Em Pauta"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");

    String nome;

    TarefaWorkflow(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
