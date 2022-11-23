package br.sc.weg.sid.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class BusBeneficiadasDemanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBusBeneficiadasDemanda;

    @JoinColumn
    @ManyToOne()
    private Demanda idDemanda;


    @JoinColumn()
    @ManyToOne()
    private BusinessUnity idBusinessUnity;

}
