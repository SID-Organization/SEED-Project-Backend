package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PAUTA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Pauta {

    @Id
    @Column(name = "IdPauta", nullable = false, unique = true)
    private Integer idPauta;

    @Column(name = "dataReuniaoPauta", nullable = false)
    private Date dataReuniaoPauta;

    @OneToOne
    @JoinColumn(name = "idComissao", nullable = false)
    private Comissao idComissao;
}
