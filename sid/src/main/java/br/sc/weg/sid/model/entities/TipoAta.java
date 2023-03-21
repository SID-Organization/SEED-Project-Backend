package br.sc.weg.sid.model.entities;

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
