package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.util.Date;
import java.util.List;

@Data
public class UpdatePropostaDTO {

    private String escopoProposta;
    private Double paybackProposta;
    private Integer aprovadoWorkflowProposta;
    private String motivoRecusaWorkflowProposta;
    private Date periodoExecucaoDemanda;
    private String naoFazParteDoEscopoProposta;
    private String alternativasAvaliadasProposta;
    private String planoMitigacaoProposta;
    private Integer custosTotaisDoProjeto;

}
