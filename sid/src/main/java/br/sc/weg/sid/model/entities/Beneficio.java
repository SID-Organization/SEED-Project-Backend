package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "BENEFICIO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JoinColumn(name = "idBeneficio", nullable = false)
    private Integer idBeneficio;

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
    @JsonIgnore
    private Demanda demandaBeneficio;
}