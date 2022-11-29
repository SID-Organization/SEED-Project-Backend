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
    private String codigoPPM;
    private String escopoProposta;
    private Double paybackProposta;
    @FutureOrPresent(message = "A data de inicio não pode ser no passado")
    private Date periodoExecucaoInicio;
    @FutureOrPresent(message = "A data de fim não pode ser no passado")
    private Date periodoExecucaoFim;
    private String linkJira;
    private Integer aprovadoWorkflowProposta;
    private Demanda idDemanda;
    private List<ResponsaveisNegocio> responsaveisNegocio;
}
