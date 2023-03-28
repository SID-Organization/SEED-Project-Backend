package br.sc.weg.sid.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "CENTRO_CUSTO_TABELA_CUSTO")
public class CentroCustoTabelaCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCentroCustoTabelaCusto", nullable = false, unique = true)
    private Integer idCentroCustoTabelaCusto;

    @ManyToOne
    @JoinColumn(name = "idCentroCusto", nullable = false)
    private CentroCusto centroCusto;

    @Column(name = "porcentagemDespesa", nullable = false)
    private Double porcentagemDespesa;

}
