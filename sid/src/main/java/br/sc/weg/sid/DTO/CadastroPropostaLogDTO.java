package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.ParecerComissao;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.TipoAta;
import lombok.Data;

@Data
public class CadastroPropostaLogDTO {

    Proposta propostaPropostaLogDTO;

    ParecerComissao parecerComissaoPropostaLogDTO;

    String consideracoesPropostaLogDTO;

    TipoAta tipoAtaPropostaLogDTO;

}
