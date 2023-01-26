package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.ResponsaveisNegocio;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class CadastroPropostaDTO {
    @NotBlank(message = "O campo codigoPPMProposta não pode estar em branco!")
    private String codigoPPMProposta;
    @NotBlank(message = "O campo escopoProposta não pode estar em branco!")
    private String escopoProposta;
    @NotNull(message = "O campo paybackProposta não pode ser nulo")
    private Double paybackProposta;
    @FutureOrPresent(message = "A data de inicio não pode ser no passado")
    private Date periodoExecucaoInicioProposta;
    @FutureOrPresent(message = "A data de fim não pode ser no passado")
    private Date periodoExecucaoFimProposta;
    @NotBlank(message = "O campo linkJiraProposta não pode estar em branco!")
    private String linkJiraProposta;
    @NotNull(message = "O campo aprovadoWorkflowProposta não pode ser nulo")
    private Integer aprovadoWorkflowProposta;
    @NotNull(message = "O campo demandaProposta não pode ser nulo")
    private Demanda demandaProposta;
    @NotNull(message = "O campo responsaveisNegocio não pode ser nulo")
    private List<ResponsaveisNegocio> responsaveisNegocio;
}
