package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.enums.ParecerComissao;
import lombok.Data;

@Data
public class CadastroParecerDGAtaDTO {
    private Integer idAta;

    private ParecerComissao parecerDGPropostaLog;

    private String consideracoesParecerDGPropostaLog;
}
