package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CENTRO_CUSTO_TABELA_CUSTO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CentroCustoTabelaCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCentroCustoTabelaCusto", nullable = false, unique = true)
    private Integer idCentroCustoTabelaCusto;

    @ManyToOne
    @JoinColumn(name = "idCentroCusto")
    private CentroCusto centroCusto;

    @Column(name = "porcentagemDespesa")
    private Double porcentagemDespesa;
}
