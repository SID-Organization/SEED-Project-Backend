package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CENTRO_CUSTO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CentroCusto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCentroCusto", nullable = false, unique = true)
    private Integer idCentroCusto;

    @Column(name = "NomeCentroCusto", nullable = false)
    private String nomeCentroCusto;
}
