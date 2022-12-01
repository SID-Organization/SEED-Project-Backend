package br.sc.weg.sid.model.entities;

public enum StatusWorkflow {
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDO("Concluído"),
    ATRASADO("Atrasado");

    private String descricao;

    StatusWorkflow(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}