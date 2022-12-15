package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.ResponsaveisNegocio;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.FutureOrPresent;
import java.util.Date;
import java.util.List;

@Data
public class CadastroPropostaDTO {
    private String codigoPPMProposta;
    private String escopoProposta;
    private Double paybackProposta;
    @FutureOrPresent(message = "A data de inicio não pode ser no passado")
    private Date periodoExecucaoInicioProposta;
    @FutureOrPresent(message = "A data de fim não pode ser no passado")
    private Date periodoExecucaoFimProposta;
    private String linkJiraProposta;
    private Integer aprovadoWorkflowProposta;
    private Demanda demandaProposta;
    private List<ResponsaveisNegocio> responsaveisNegocio;
}
