package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.enums.ParecerComissao;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.enums.TipoAta;
import lombok.Data;

@Data
public class CadastroPropostaLogDTO {

    Proposta propostaPropostaLog;

    ParecerComissao parecerComissaoPropostaLog;

    String consideracoesPropostaLog;

    ParecerComissao parecerDGPropostaLog;

    String consideracoesParecerDGPropostaLog;

    TipoAta tipoAtaPropostaLog;

}
