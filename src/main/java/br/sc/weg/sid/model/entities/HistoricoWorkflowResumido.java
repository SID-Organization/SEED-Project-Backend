package br.sc.weg.sid.model.entities;

import lombok.Data;

import java.util.Date;

@Data
public class HistoricoWorkflowResumido {

    private Integer idHistoricoWorkflow;

    private Date recebimentoHistorico;

    private Date conclusaoHistorico;

    private Date prazoHistorico;

    private String tarefaHistoricoWorkflow;

    private String nomeResponsavel;

    private String numeroCadastroResponsavel;

    private String acaoFeitaHistorico;

    private String statusWorkflow;

    private String motivoDevolucaoHistorico;

    private Double versaoHistorico;

}
