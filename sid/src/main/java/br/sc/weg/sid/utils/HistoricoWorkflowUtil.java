package br.sc.weg.sid.utils;

import br.sc.weg.sid.model.entities.HistoricoWorkflow;
import br.sc.weg.sid.model.entities.HistoricoWorkflowResumido;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class HistoricoWorkflowUtil {
    private ObjectMapper mapper = new ObjectMapper();

    public static List<HistoricoWorkflowResumido> converterHistoricoWorkflowParaHistoricoWorkflowReumido(List<HistoricoWorkflow> historicoWorkflows) {
        List<HistoricoWorkflowResumido> historicoWorkflowResumidos = new ArrayList<>();
        historicoWorkflows.forEach(historicoWorkflow -> {
            HistoricoWorkflowResumido historicoWorkflowResumido = new HistoricoWorkflowResumido();
            BeanUtils.copyProperties(historicoWorkflow, historicoWorkflowResumido);
            historicoWorkflowResumido.setNomeResponsavel(historicoWorkflow.getIdResponsavel().getNomeUsuario());
            historicoWorkflowResumido.setRecebimentoHistorico(historicoWorkflow.getRecebimentoHistorico());
            historicoWorkflowResumidos.add(historicoWorkflowResumido);
        });
        return historicoWorkflowResumidos;
    };

}
