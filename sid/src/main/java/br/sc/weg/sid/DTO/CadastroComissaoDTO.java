package br.sc.weg.sid.DTO;

import lombok.Data;

import javax.persistence.Column;

@Data
public class CadastroComissaoDTO {

    private String nomeComissao;
    private String siglaComissao;

}
