package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.util.Date;

@Entity
@Table(name = "PROPOSTA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IdProposta", nullable = false, unique = true)
    private Integer idProposta;

    @Column(name = "CodigoPPM", nullable = false)
    private String codigoPPMProposta;

    @Column(name = "EscopoProposta", nullable = false, length = 4000)
    private String escopoProposta;

    @Column(name = "PaybackProposta", nullable = false)
    private Double paybackProposta;

    @FutureOrPresent
    @Column(name = "PeriodoExecucaoInicioProposta", nullable = false)
    private Date periodoExecucaoInicioProposta;

    @FutureOrPresent
    @Column(name = "PeriodoExecucaoFimProposta", nullable = false)
    private Date periodoExecucaoFimProposta;

    @Column(length = 255, name = "LinkJiraProposta", nullable = false)
    private String linkJiraProposta;

    @Column(name = "AprovadoWorkflowProposta", nullable = false)
    private Integer aprovadoWorkflowProposta;

    @Column(name = "MotivoRecusaProposta")
    private String motivoRecusaWorkflowProposta;

    @JoinColumn(name = "demandaProposta", referencedColumnName = "IdDemanda")
    @ManyToOne(optional = false)
    private Demanda demandaProposta;
}