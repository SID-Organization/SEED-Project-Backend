package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalTime;
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

    @JsonFormat(pattern = "HH:mm:ss")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @Column(name = "horarioInicioPauta", nullable = false)
    private LocalTime horarioInicioPauta;

    @JsonFormat(pattern = "HH:mm:ss")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @Column(name = "horarioTerminoPauta", nullable = false)
    private LocalTime horarioTerminoPauta;

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
