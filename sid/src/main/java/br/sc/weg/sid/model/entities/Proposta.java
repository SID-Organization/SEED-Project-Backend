package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
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
    private String codigoPPM;

    @Column(name = "EscopoProposta", nullable = false, length = 4000)
    private String escopoProposta;

    @Column(name = "PaybackProposta", nullable = false)
    private Double paybackProposta;

    @Column(name = "PeriodoExecucaoInicioProposta", nullable = false)
    private Date periodoExecucaoInicio;

    @Column(name = "PeriodoExecucaoFimProposta", nullable = false)
    private Date periodoExecucaoFim;

    @Column(length = 255, name = "LinkJiraProposta", nullable = false)
    private String linkJira;

    @Column(name = "AprovadoWorkflowProposta", nullable = false)
    private Integer aprovadoWorkflowProposta;

    @Column(name = "MotivoRecusaProposta", nullable = false)
    private String motivoRecusaProposta;

    @JoinColumn(name = "idDemanda", nullable = false)
    @OneToOne
    private Demanda idDemanda;
}
