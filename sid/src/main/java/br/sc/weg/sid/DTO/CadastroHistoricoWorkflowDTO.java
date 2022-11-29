package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.StatusWorkflow;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Data
public class CadastroHistoricoWorkflowDTO {

    @PastOrPresent
    private Date recebimentoHistorico;

    @FutureOrPresent
    private Date prazoHistorico;

    private String tarefaHistorico;

    private byte[] pdfHistorico;

    private Demanda idDemanda;

    private Usuario idResponsavel;

}
