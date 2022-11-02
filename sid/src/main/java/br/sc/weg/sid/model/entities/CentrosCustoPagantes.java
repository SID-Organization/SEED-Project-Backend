package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CENTROS_CUSTO_PAGANTES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CentrosCustoPagantes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCentrosCustoPagantes", nullable = false, unique = true)
    private Integer idCentrosCustoPagantes;

    @ManyToOne
    @JoinColumn(name = "idTabelaCusto", nullable = false)
    private TabelaCusto idTabelaCusto;

    @ManyToOne
    @JoinColumn(name = "idCentroCusto", nullable = false)
    private CentroCusto idCentroCusto;

    @Column(name = "porcentagemPagamentoCentroCusto", nullable = false)
    private Double porcentagemPagamentoCentroCusto;
}
