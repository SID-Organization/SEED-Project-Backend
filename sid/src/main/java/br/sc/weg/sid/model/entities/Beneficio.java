package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "BENEFICIO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "idBeneficio", nullable = false)
    private Integer idBeneficio;

    @Column(name = "idFront")
    private Integer idFront;

    @Enumerated(EnumType.STRING)
    @Column(name = "MoedaBeneficio")
    private Moeda moedaBeneficio;

    @Column(name = "valorBeneficio")
    private Double valorBeneficio;

    @Column(name = "MemoriaCalculoBeneficio")
    private String memoriaCalculoBeneficio;

    @Column(name = "MemoriaCalculoBeneficioHTML")
    private String memoriaCalculoBeneficioHTML;

    @Column(name = "TipoBeneficio", nullable = false)
    private TipoBeneficio tipoBeneficio;

    @Column(name = "DescricaoBeneficio")
    private String descricaoBeneficio;

    @Column(name = "DescricaoBeneficioHTML")
    private String descricaoBeneficioHTML;

    @ManyToOne()
    @JoinColumn(name = "idDemanda")
    @JsonIgnoreProperties("beneficiosDemanda")
    private Demanda demandaBeneficio;
}