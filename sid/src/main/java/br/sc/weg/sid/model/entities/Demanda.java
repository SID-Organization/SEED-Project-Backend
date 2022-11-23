package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.utils.DemandaUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DEMANDA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Demanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 50, nullable = false, unique = true)
    private Integer idDemanda;

    @Column(length = 100)
    private String tituloDemanda;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status statusDemanda;


    @Enumerated(EnumType.STRING)
    @Column()
    private Tamanho tamanhoDemanda;

    @Column(length = 4000)
    private String propostaDemanda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Secao secaoTIResponsavel;

    @Column()
    private Integer frequenciaUsoDemanda;

    @Column(length = 400)
    private String descricaoQualitativoDemanda;

    @Column()
    private Date prazoElaboracaoDemanda;

    @Column()
    private Integer codigoPPM;

    @Column
    private String linkJira;

    @Column()
    private Double scoreDemanda;


    @Column(length = 6000)
    private String situacaoAtualDemanda;


//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id_bu_solicitante")
//    private BusinessUnity idBuSolicitante;

    @ManyToOne()
    @JoinColumn(name = "numero_cadastro_usuario")
    private Usuario solicitanteDemanda;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "idProposta")
//    private Proposta idProposta;


    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "idDemanda")
    private List<BusBeneficiadasDemanda> busBeneficiadas;

    @OneToMany(mappedBy = "idDemanda",cascade = CascadeType.ALL)
    private List<ArquivoDemanda> arquivosDemandas = new ArrayList<>();

    @OneToMany(mappedBy = "idCentroCusto")
    private List<CentroCusto> centroCusto;

    @OneToMany(mappedBy = "idDemanda", cascade = CascadeType.REMOVE)
    private List<Beneficio> beneficios;
}