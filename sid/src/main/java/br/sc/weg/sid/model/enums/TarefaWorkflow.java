package br.sc.weg.sid.model.enums;

public enum TarefaWorkflow {
    PREENCHER_DEMANDA("Preencher Demanda"),
    CLASSIFICACAO_APROVACAO("Classificação e Aprovação"),

    EDITAR_DEMANDA("Editando Demanda"),
    APROVACAO_GERENTE_AREA("Aprovação Gerente da Área"),
    ELABORACAO_PROPOSTA("Elaboração da Proposta"),
    APROVACAO_COMISSAO("Aprovação Comissão"),
    APROVACAO_DG("Aprovação DG"),
    EXECUCAO_PROPOSTA("Execução da Proposta");


    String nome;

    TarefaWorkflow(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
