package br.sc.weg.sid.model.entities;

import lombok.Data;

import java.util.Date;

@Data
public class HistoricoWorkflowResumido {

    private Date recebimentoHistorico;

    private Date conclusaoHistorico;

    private Date prazoHistorico;

    private TarefaWorkflow tarefaHistoricoWorkflow;

    private String nomeResponsavel;

    private String acaoFeitaHistorico;

    private StatusWorkflow statusWorkflow;

    private Double versaoHistorico;

}
