package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "BUS_BENEFICIADAS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BusBeneficiadas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBusBeneficiadas", nullable = false, unique = true)
    private Integer idBusBeneficiadas;

    @ManyToOne
    @JoinColumn(name = "idBusinessUnity", nullable = false)
    private BusinessUnity idBusinessUnity;

    @ManyToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda idDemanda;
}
