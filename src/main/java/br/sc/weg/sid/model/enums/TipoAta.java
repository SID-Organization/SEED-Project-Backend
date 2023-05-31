package br.sc.weg.sid.model.enums;

import lombok.Getter;

@Getter
public enum TipoAta {
    PUBLICADA("Publicada"),
    NAO_PUBLICADA("Não publicada");

    private String descricao;

    TipoAta(String descricao) {
        this.descricao = descricao;
    }
}
