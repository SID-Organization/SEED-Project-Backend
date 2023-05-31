package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Proposta;
import lombok.Data;

@Data
public class CadastroPdfPropostaDTO {

    private String escopoPropostaHTML;

    private String naoFazParteDoEscopoPropostaHTML;

    private String alternativasAvaliadasPropostaHTML;

    private String planoMitigacaoPropostaHTML;

    private String abrangenciaProjetoPropostaHTML;

    private Proposta proposta;

}
