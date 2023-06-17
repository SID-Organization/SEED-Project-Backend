package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.model.enums.StatusDemanda;
import br.sc.weg.sid.model.enums.TamanhoDemanda;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DEMANDA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Demanda {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer idDemanda;

    @Column()
    private String tituloDemanda;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDemanda statusDemanda;

    @Enumerated(EnumType.STRING)
    @Column()
    private TamanhoDemanda tamanhoDemanda;

    @Column(length = 999999999)
    private String propostaMelhoriaDemanda;

    @Column()
    private String frequenciaUsoDemanda;

    @Column(length = 999999999)
    private String descricaoQualitativoDemanda;

    @Column()
    private Date prazoElaboracaoDemanda;

    @Column()
    private Date dataCriacaoDemanda;

    @Column()
    private Integer codigoPPMDemanda;

    @Column
    private String linkJiraDemanda;

    @Column()
    private Double scoreDemanda;

    @OneToOne()
    @JoinColumn(name = "analistaResponsavelDemanda")
    private Usuario analistaResponsavelDemanda;

    @OneToOne()
    @JoinColumn(name = "gerenteDaArea")
    private Usuario gerenteDaAreaDemanda;

    @OneToOne()
    @JoinColumn(name = "gestorResponsavel")
    private Usuario gestorResponsavelDemanda;

    Double custoTotalDemanda;

    @OneToOne
    @ToString.Exclude
    private HistoricoWorkflow historicoWorkflowUltimaVersao;

    @Column(length = 999999999)
    private String situacaoAtualDemanda;

    @OneToMany(mappedBy = "demandaMotivoRecusa", cascade = CascadeType.ALL)
    private List<MotivoRecusa> motivosRecusaDemanda;

    @Column(name = "pdfDemanda")
    @Lob
    private byte[] pdfDemanda;

    @JoinColumn(name = "idBuSolicitante")
    @ManyToOne()
    private BusinessUnity buSolicitanteDemanda;

    @ManyToOne()
    @JoinColumn(name = "secaoTIResponsavel")
    private SecaoTIResponsavel secaoTIResponsavelDemanda;

    @ManyToOne()
    @JoinColumn(name = "numero_cadastro_usuario")
    private Usuario solicitanteDemanda;

    @ManyToMany()
    @JoinTable(name = "busBeneficiadasDemanda"
            , joinColumns = @JoinColumn(name = "idDemanda")
            , inverseJoinColumns = @JoinColumn(name = "idBusinessUnity"))
    @ToString.Exclude
    private List<BusinessUnity> busBeneficiadasDemanda;

    @OneToMany(mappedBy = "idDemanda",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ArquivoDemanda> arquivosDemandas = new ArrayList<>();

    @OneToMany(mappedBy = "idCentroCusto")
    private List<CentroCusto> centroCustoDemanda;

    @OneToMany(mappedBy = "demandaBeneficio", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Beneficio> beneficiosDemanda;
}