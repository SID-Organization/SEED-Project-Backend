package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "BENEFICIO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "idBeneficio", nullable = false)
    private Integer idBeneficio;

    @Enumerated(EnumType.STRING)
    @Column(name = "MoedaBeneficio")
    private Moeda moedaBeneficio;

    @Column(name = "valorBeneficio")
    private Double valorBeneficio;

    @Column(name = "MemoriaCalculoBeneficio")
    private Double memoriaCalculoBeneficio;

    @Column(length = 4000, name = "DescricaoBeneficio", nullable = false)
    private String descricaoBeneficio;

    @Column(columnDefinition = "TEXT", name = "DescricaoBeneficioDELTA")
    private String descricaoBeneficioDELTA;

    @Column(name = "TipoBeneficio", nullable = false)
    private TipoBeneficio tipoBeneficio;

    @ManyToOne()
    @JoinColumn(name = "idDemanda")
    @JsonIgnore
    private Demanda demandaBeneficio;
}