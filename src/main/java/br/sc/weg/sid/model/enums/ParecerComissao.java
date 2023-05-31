package br.sc.weg.sid.model.enums;

import lombok.Getter;

@Getter
public enum ParecerComissao {
    APROVADO("Aprovado"),
    REPROVADO("Reprovado"),
    MAIS_INFORMACOES("Mais informações"),
    BUSINESS_CASE("Business Case");

    private String descricao;

    ParecerComissao(String descricao) {
        this.descricao = descricao;
    }
}
