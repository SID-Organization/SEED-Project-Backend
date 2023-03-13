package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.utils.ConverterUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(length = 50, nullable = false, unique = true)
    private Integer idDemanda;

    @Column(length = 100)
    private String tituloDemanda;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDemanda statusDemanda;

    @Enumerated(EnumType.STRING)
    @Column()
    private TamanhoDemanda tamanhoDemanda;

    @Column(length = 4000)
    private String propostaMelhoriaDemanda;

    @Column()
    private String secaoTIResponsavelDemanda;

    @Column()
    private String frequenciaUsoDemanda;

    @Column(length = 400)
    private String descricaoQualitativoDemanda;

    @Column()
    private Date prazoElaboracaoDemanda;

    @Column()
    private Integer codigoPPMDemanda;

    @Column
    private String linkJiraDemanda;

    @Column()
    private Double scoreDemanda;

    @OneToOne
    @JoinColumn(name = "analistaResponsavelDemanda")
    private Usuario analistaResponsavelDemanda;

    @OneToOne
    @JoinColumn(name = "gerenteDaArea")
    private Usuario gerenteDaAreaDemanda;

    @OneToOne
    @JoinColumn(name = "gestorResponsavel")
    private Usuario gestorResponsavelDemanda;

    @OneToOne
    @JsonIgnore
    HistoricoWorkflow historicoWorkflowUltimaVersao;

    @Column(length = 6000)
    private String situacaoAtualDemanda;

    @JoinColumn(name = "idBuSolicitante")
    private String buSolicitanteDemanda;

    @ManyToOne()
    @JoinColumn(name = "numero_cadastro_usuario")
    private Usuario solicitanteDemanda;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idProposta")
    private Proposta propostaElaboradaDemanda;

    @ManyToMany()
    @JoinTable(name = "busBeneficiadasDemanda"
            , joinColumns = @JoinColumn(name = "idDemanda")
            , inverseJoinColumns = @JoinColumn(name = "idBusinessUnity"))
    private List<BusinessUnity> busBeneficiadasDemanda;

    @OneToMany(mappedBy = "idDemanda",cascade = CascadeType.ALL)
    private List<ArquivoDemanda> arquivosDemandas = new ArrayList<>();

    @OneToMany(mappedBy = "idCentroCusto")
    private List<CentroCusto> centroCustoDemanda;

    @OneToMany(mappedBy = "demandaBeneficio", cascade = CascadeType.REMOVE)
    private List<Beneficio> beneficiosDemanda;
}