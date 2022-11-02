package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CENTRO_CUSTO_DEMANDA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CentroCustoDemanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCentroCustoDemanda", nullable = false, unique = true)
    private Integer idCentroCustoDemanda;

    @ManyToOne
    @JoinColumn(name = "idCentroCusto", nullable = false)
    private CentroCusto idCentroCusto;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda idDemanda;
}
