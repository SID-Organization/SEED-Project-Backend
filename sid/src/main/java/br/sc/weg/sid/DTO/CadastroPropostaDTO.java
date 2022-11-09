package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.ResponsaveisNegocio;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter @Setter @ToString
public class CadastroPropostaDTO {
    private String codigoPPM;
    private String escopoProposta;
    private Double paybackProposta;
    private Date periodoExecucaoInicio;
    private Date periodoExecucaoFim;
    private String linkJira;
    private Integer aprovadoWorkflowProposta;
    private List<ResponsaveisNegocio> responsaveisNegocio;
}
