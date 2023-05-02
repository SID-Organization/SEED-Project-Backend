package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.enums.TarefaWorkflow;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CadastroHistoricoWorkflowDTO {
    @NotNull(message = "O campo tarefaHistoricoWorkflow não pode ser nulo")
    private TarefaWorkflow tarefaHistoricoWorkflow;
    @NotNull(message = "O campo demandaHistorico não pode ser nulo")
    private Demanda demandaHistorico;
    @NotBlank(message = "O campo acaoFeitaHistorico não pode estar em branco!")
    private String acaoFeitaHistorico;
    @NotNull(message = "O campo idResponsavel não pode ser nulo")
    private Usuario idResponsavel;

}
