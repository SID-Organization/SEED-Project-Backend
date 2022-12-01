package br.sc.weg.sid.model.entities;

import lombok.Data;

import java.util.Date;

@Data
public class HistoricoWorkflowResumido {

    private Date prazoHistorico;

    private TarefaWorkflow tarefaHistoricoWorkflow;

    private StatusWorkflow statusWorkflow;

    private String nomeResponsavel;

    private String acaoFeitaHistorico;

    private double versaoHistorico;

}
