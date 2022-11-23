package br.sc.weg.sid.model.entities;

public enum StatusWorkflow {
    ABERTO("Aberto"),
    EM_ANDAMENTO("Em andamento"),
    CANCELADO("Cancelado");

    private String descricao;

    StatusWorkflow(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}