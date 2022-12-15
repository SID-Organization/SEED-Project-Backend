package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.TarefaWorkflow;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;
import javax.validation.constraints.FutureOrPresent;
import java.util.Date;

@Data
public class CadastroHistoricoWorkflowDTO {

    private TarefaWorkflow tarefaHistoricoWorkflow;

    private Demanda demandaHistorico;

    private String acaoFeitaHistorico;

    private Usuario idResponsavel;

}
