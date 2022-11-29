package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HISTORICO_WORKFLOW")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoWorkflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdHistoricoWorkflow", nullable = false, unique = true)
    private Integer idHistoricoWorkflow;

    @Column(name = "RecebimentoHistorico", nullable = false)
    private Date recebimentoHistorico;

    @Column(name = "PrazoHistorico", nullable = false)
    private Date prazoHistorico;

    @Column(name = "TarefaHistorico", nullable = false)
    private String tarefaHistorico;

    @Column(name = "StatusHistorico", nullable = false)
    private StatusWorkflow statusWorkflow;

    @Column(name = "PdfHistorico", nullable = false)
    @Lob
    private byte[] pdfHistorico;

    @Column(name = "MotivoDevolucaoHistorico")
    private String motivoDevolucaoHistorico;

    @Column(name = "VersaoHistorico")
    private Double versaoHistorico;

    @Column(name = "ConclusaoHistorico")
    private Date conclusaoHistorico;

    @Column(name = "AcaoFeitaHistorico")
    private String acaoFeitaHistorico;

    @JoinColumn(name = "idDemanda", nullable = false)
    @ManyToOne
    private Demanda idDemanda;

    @JoinColumn(name = "idUsuario", nullable = false)
    @ManyToOne
    private Usuario idResponsavel;
}
