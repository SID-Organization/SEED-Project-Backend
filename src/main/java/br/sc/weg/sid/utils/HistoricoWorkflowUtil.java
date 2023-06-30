package br.sc.weg.sid.utils;

import br.sc.weg.sid.model.entities.HistoricoWorkflow;
import br.sc.weg.sid.model.entities.HistoricoWorkflowResumido;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoricoWorkflowUtil {
    private ObjectMapper mapper = new ObjectMapper();

    public static List<HistoricoWorkflowResumido> converterHistoricoWorkflowParaHistoricoWorkflowReumido(List<HistoricoWorkflow> historicoWorkflows) {
        List<HistoricoWorkflowResumido> historicoWorkflowResumidos = new ArrayList<>();
        historicoWorkflows.forEach(historicoWorkflow -> {
            HistoricoWorkflowResumido historicoWorkflowResumido = new HistoricoWorkflowResumido();
            BeanUtils.copyProperties(historicoWorkflow, historicoWorkflowResumido);
            historicoWorkflowResumido.setIdHistoricoWorkflow(historicoWorkflow.getIdHistoricoWorkflow());
            if (historicoWorkflow.getIdResponsavel() != null){
                historicoWorkflowResumido.setNomeResponsavel(historicoWorkflow.getIdResponsavel().getNomeUsuario());
                historicoWorkflowResumido.setNumeroCadastroResponsavel(historicoWorkflow.getIdResponsavel().getNumeroCadastroUsuario().toString());
            }
            historicoWorkflowResumido.setRecebimentoHistorico(historicoWorkflow.getRecebimentoHistorico());
            historicoWorkflowResumido.setTarefaHistoricoWorkflow(historicoWorkflow.getTarefaHistoricoWorkflow().getNome());
            if (historicoWorkflow.getMotivoDevolucaoHistorico() != null) {
                historicoWorkflowResumido.setMotivoDevolucaoHistorico(historicoWorkflow.getMotivoDevolucaoHistorico());
            }
            historicoWorkflowResumido.setStatusWorkflow(historicoWorkflow.getStatusWorkflow().getDescricao());
            DecimalFormat df = new DecimalFormat("0.0");

            df.setRoundingMode(RoundingMode.CEILING);
            String format = df.format(historicoWorkflow.getVersaoHistorico());
            format = format.replace(",", ".");
            historicoWorkflowResumido.setVersaoHistorico(new BigDecimal(format));
            historicoWorkflowResumidos.add(historicoWorkflowResumido);
        });
        return historicoWorkflowResumidos;
    }

    ;

}
