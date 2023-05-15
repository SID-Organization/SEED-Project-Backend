package br.sc.weg.sid.model.enums;

public enum StatusWorkflow {
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDO("Concluído"),
    ATRASADO("Atrasado"),
    CANCELADO("Cancelado");

    private String descricao;

    StatusWorkflow(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}