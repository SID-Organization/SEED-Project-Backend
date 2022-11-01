package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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

    @JoinColumn(name = "id_bu_solicitante")
    @OneToOne
    private Bu buSolicitante;

    @JoinColumn(name = "id_usuario_solicitante")
    @OneToOne
    private Usuario usuarioSolicitante;
}
