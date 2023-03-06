package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PROPOSTA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IdProposta", nullable = false, unique = true)
    private Integer idProposta;

    @Column(name = "CodigoPPM", nullable = false)
    private String codigoPPMProposta;

    @Column(name = "EscopoProposta", length = 4000)
    private String escopoProposta;

    @Column(name = "NaoFazParteDoEscopoProposta", length = 4000)
    private String naoFazParteDoEscopoProposta;

    @Column(name = "AlternativasAvaliadasProposta", length = 4000)
    private String alternativasAvaliadasProposta;

    @Column(name = "PlanoMitigacaoProposta", columnDefinition = "TEXT")
    private String planoMitigacaoProposta;

    @Column(name = "PaybackProposta")
    private Double paybackProposta;

    @FutureOrPresent
    @Column(name = "PeriodoExecucaoInicioProposta", nullable = false)
    private Date periodoExecucaoInicioProposta;

    @FutureOrPresent
    @Column(name = "PeriodoExecucaoFimProposta", nullable = false)
    private Date periodoExecucaoFimProposta;

    @Column(length = 255, name = "LinkJiraProposta", nullable = false)
    private String linkJiraProposta;

    @Column(name = "AprovadoWorkflowProposta")
    private Integer aprovadoWorkflowProposta;

    @Column(name = "MotivoRecusaProposta")
    private String motivoRecusaWorkflowProposta;

    @Column(name = "PropostaMarkdown")
    private String propostaMarkdown;

    @Column(name = "PropostaPDF")
    private byte[] propostaPDF;

    @FutureOrPresent
    @Column(name = "PeriodoExecucaoDemanda")
    private Date periodoExecucaoDemanda;

    @JoinColumn(name = "demandaProposta", referencedColumnName = "IdDemanda")
    @ManyToOne(optional = false)
    private Demanda demandaProposta;

    @ManyToMany
    @JoinTable(name = "responsaveis_negocio",
            joinColumns = @JoinColumn(name = "idProposta"),
            inverseJoinColumns = @JoinColumn(name = "numeroCadastroUsuario"))
    private List<Usuario> responsaveisNegocio;

}

