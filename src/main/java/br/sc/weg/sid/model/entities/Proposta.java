package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdProposta", nullable = false, unique = true)
    private Integer idProposta;

    @Column(name = "CodigoPPM", nullable = false)
    private String codigoPPMProposta;

    //deve ser um longtext
    @Lob
    @Column(name = "DescricaoProposta", columnDefinition = "TEXT")
    private String escopoProposta;

    @Lob
    @Column(name = "NaoFazParteDoEscopoProposta", columnDefinition = "TEXT")
    private String naoFazParteDoEscopoProposta;

    @Lob
    @Column(name = "AbrangenciaProjetoProposta", columnDefinition = "TEXT")
    private String abrangenciaProjetoProposta;

    @Lob
    @Column(name = "AlternativasAvaliadasProposta", columnDefinition = "TEXT")
    private String alternativasAvaliadasProposta;

    @Lob
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

    @Column(name = "LinkJiraProposta", nullable = false)
    private String linkJiraProposta;

    @Column(name = "PdfProposta")
    @Lob
    private byte[] pdfProposta;

    @Column(name = "AprovadoWorkflowProposta")
    private Integer aprovadoWorkflowProposta;

    @Column(name = "CustosTotaisDoProjeto")
    private Double custosTotaisDoProjeto;

    @Column(name = "CustosInternosDoProjeto")
    private Double custosInternosDoProjeto;

    @Column(name = "CustosExternosDoProjeto")
    private Double custosExternosDoProjeto;

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

    @OneToMany(mappedBy = "propostaTabelaCusto", cascade = CascadeType.ALL)
    private List<TabelaCusto> tabelaCusto;

    @JoinColumn(name = "demandaProposta", referencedColumnName = "IdDemanda")

    @ManyToOne(optional = false)
    private Demanda demandaProposta;

    @ManyToMany
    @JoinTable(name = "responsaveis_negocio",
            joinColumns = @JoinColumn(name = "idProposta"),
            inverseJoinColumns = @JoinColumn(name = "numeroCadastroUsuario"))
    @ToString.Exclude
    private List<Usuario> responsaveisNegocio;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pauta_proposta",
            joinColumns = @JoinColumn(name = "idProposta"),
            inverseJoinColumns = @JoinColumn(name = "idPauta"))
    @ToString.Exclude
    private List<Pauta> pautaProposta;

}

