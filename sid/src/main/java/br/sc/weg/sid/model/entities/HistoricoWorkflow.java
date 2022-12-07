package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HISTORICO_WORKFLOW")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdHistoricoWorkflow", nullable = false, unique = true)
    private Integer idHistoricoWorkflow;

    @Column(name = "RecebimentoHistorico", nullable = false)
    private Date recebimentoHistorico;

    @Column(name = "PrazoHistorico")
    private Date prazoHistorico;

    @Column(name = "StatusHistorico", nullable = false)
    private StatusWorkflow statusWorkflow;

    @EqualsAndHashCode.Include
    @Column(name = "TarefaHistoricoWorkflow", nullable = false)
    private TarefaWorkflow tarefaHistoricoWorkflow;

    @Column(name = "PdfHistorico")
    @Lob
    private byte[] pdfHistorico;

    @Column(name = "MotivoDevolucaoHistorico")
    private String motivoDevolucaoHistorico;

    @Column(name = "VersaoHistorico")
    private Double versaoHistorico;

    @Column(name = "ConclusaoHistorico")
    private Date conclusaoHistorico;

    @EqualsAndHashCode.Exclude
    @Column(name = "AcaoFeitaHistorico")
    private String acaoFeitaHistorico;

    @EqualsAndHashCode.Include
    @JoinColumn(name = "DemandaHistorico", nullable = false)
    @ManyToOne
    private Demanda demandaHistorico;

    @JoinColumn(name = "idUsuario", nullable = false)
    @ManyToOne
    private Usuario idResponsavel;
}
