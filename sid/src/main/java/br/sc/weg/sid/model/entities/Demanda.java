package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.utils.DemandaUtil;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Time;
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

    @Column(length = 100, nullable = false)
    private String tituloDemanda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status statusDemanda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tamanho tamanhoDemanda;

    @Column()
    private Double scoreDemanda;

    @Column(length = 4000)
    private String propostaDemanda;

    @Column(length = 4000)
    private String objetivoDemanda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Secao secaoTIResponsavel;

    @Column(length = 6000)
    private String situacaoAtualDemanda;

    @Column()
    private Integer frequenciaUsoDemanda;

    @Column(length = 400)
    private String descricaoQualitativoDemanda;

    @Column()
    private Date prazoElaboracaoDemanda;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id_bu_solicitante")
//    private BusinessUnity idBuSolicitante;

    @ManyToOne()
    @JoinColumn(name = "numero_cadastro_usuario")
    private Usuario solicitanteDemanda;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "idProposta")
//    private Proposta idProposta;

    @OneToMany(mappedBy = "idDemanda")
    private List<BusBeneficiadas> busBeneficiadas;

    @OneToMany(mappedBy = "idDemanda")
    private List<Beneficio> beneficios;
}