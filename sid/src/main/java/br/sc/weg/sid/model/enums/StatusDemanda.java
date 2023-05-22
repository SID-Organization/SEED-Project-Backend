package br.sc.weg.sid.model.enums;

public enum StatusDemanda {
    CLASSIFICADO_PELO_ANALISTA("Classificado pelo analista"),
    ABERTA("Aberta"),
    APROVADO_PELO_GERENTE_DA_AREA("Aprovado pelo gerente da área"),
    PROPOSTA_EM_ELABORACAO("Proposta em elaboração"),
    PROPOSTA_PRONTA("Proposta pronta"),
    EM_PAUTA("Em pauta"),
    APROVADA_EM_COMISSAO("Aprovada em comissão"),
    APROVADA_EM_DG("Aprovada em DG"),
    PROPOSTA_EM_EXECUCAO("Proposta em execução"),
    BUSINESS_CASE("Business Case"),
    PROPOSTA_EM_SUPORTE("Proposta em suporte"),
    PROPOSTA_FINALIZADA("Proposta finalizada"),
    CANCELADA("Cancelada"),
    RASCUNHO("Rascunho");

    String nome;
    StatusDemanda(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
