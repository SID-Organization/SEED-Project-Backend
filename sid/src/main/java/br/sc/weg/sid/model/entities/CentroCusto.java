package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CENTRO_CUSTO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CentroCusto {
    @Id
    @Column(name = "IdCentroCusto", nullable = false, unique = true)
    private Integer idCentroCusto;

    @Column(name = "NomeCentroCusto", nullable = false)
    private String nomeCentroCusto;
}
