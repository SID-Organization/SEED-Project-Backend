package br.sc.weg.sid.model.entities;

import lombok.*;
import org.springframework.lang.Nullable;

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

    @Column(name = "CustosTotaisDoProjeto")
    private Double custosTotaisDoProjeto;

    @Column(name = "CustosInternosDoProjeto")
    private Double custosInternosDoProjeto;

    @Column(name = "CustosExternosDoProjeto")
    private Double custosExternosDoProjeto;

    @Column(name = "MotivoRecusaProposta")
    private String motivoRecusaWorkflowProposta;
    @FutureOrPresent
    @Column(name = "PeriodoExecucaoDemanda")
    private Date periodoExecucaoDemanda;

    @FutureOrPresent
    @Column(name = "PeriodoExecucaoDemandaInicio")
    private Date periodoExecucaoDemandaInicio;

    @FutureOrPresent
    @Column(name = "PeriodoExecucaoDemandaFim")
    private Date periodoExecucaoDemandaFim;

    @Column(name = "NomeResponsavelNegocio")
    private String nomeResponsavelNegocio;

    @Column(name = "AreaResponsavelNegocio")
    private String areaResponsavelNegocio;


    @JoinColumn(name = "CentroCusto", referencedColumnName = "IdCentroCusto", nullable = true)
    @ManyToOne(optional = true)
    private CentroCusto centroCusto;

    @JoinColumn(name = "demandaProposta", referencedColumnName = "IdDemanda")
    @ManyToOne(optional = false)
    private Demanda demandaProposta;

    @ManyToMany
    @JoinTable(name = "responsaveis_negocio",
            joinColumns = @JoinColumn(name = "idProposta"),
            inverseJoinColumns = @JoinColumn(name = "numeroCadastroUsuario"))
    private List<Usuario> responsaveisNegocio;

    @ManyToMany
    @JoinTable(name = "pauta_proposta",
            joinColumns = @JoinColumn(name = "idProposta"),
            inverseJoinColumns = @JoinColumn(name = "idPauta"))
    private List<Pauta> pautaProposta;

}

