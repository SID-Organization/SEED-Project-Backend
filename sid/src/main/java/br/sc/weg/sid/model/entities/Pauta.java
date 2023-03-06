package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PAUTA")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPauta", nullable = false, unique = true)
    private Integer idPauta;

    @Column(name = "dataReuniaoPauta", nullable = false)
    private Date dataReuniaoPauta;

    @Column(name = "horarioInicioPauta", nullable = false)
    private Date horarioInicioPauta;

    @Column(name = "horarioTerminoPauta", nullable = false)
    private Date horarioTerminoPauta;

    @Column(name = "comissaoPauta", nullable = false)
    private String comissaoPauta;

    @ManyToOne()
    @JoinColumn(name = "idForum", nullable = false)
    private Forum forumPauta;

    @ManyToMany
    @JoinTable(name = "proposta_pauta",
            joinColumns = @JoinColumn(name = "idPauta"),
            inverseJoinColumns = @JoinColumn(name = "idProposta"))
    private List<Proposta> propostasPauta;
}
