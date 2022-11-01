package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "BUS_BENEFICIADAS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BusBeneficiadas {
    @JoinColumn(name = "idBu", nullable = false)
    private Bu idBu;

    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda idDemanda;
}
