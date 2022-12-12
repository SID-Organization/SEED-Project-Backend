package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @JoinColumn(name = "idDemanda")
    @ManyToOne()
    private Demanda demandaBusBeneficiadas;

    @JoinColumn(name = "idBusinessUnity")
    @ManyToOne()
    private BusinessUnity businessUnityBeneficiada;

}
