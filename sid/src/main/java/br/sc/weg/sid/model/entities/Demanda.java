package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.utils.DemandaUtil;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
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
    @Column(length = 50, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idDemanda;

    @Column(length = 100, nullable = false)
    private String tituloDemanda;

    @Column()
    private Status statusDemanda;

    @Column()
    private Tamanho tamanhoDemanda;

    @Column(length = 4000)
    private String objetivoDemanda;

    @Column(length = 60)
    private String sessaoTIResponsavel;

    @Column()
    private String situacaoAtualDemanda;

    @Column()
    private Integer frequenciaUsoDemanda;

    @Column(length = 400)
    private String descricaoQualitativoDemanda;

    @Column()
    private Date prazoElaboracaoDemanda;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_bu_solicitante")
    private BusinessUnity idBuSolicitante;

    @JoinColumn(name = "id_usuario")
    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario idUsuario;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "idChat")
//    private Chat idChat;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "idProposta")
//    private Proposta idProposta;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "bus_beneficiadas")
//    private List<BusinessUnity> bus_beneficiadas;

}
