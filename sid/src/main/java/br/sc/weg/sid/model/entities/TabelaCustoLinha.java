package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TABELA_CUSTO_LINHA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class TabelaCustoLinha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTabelaCustoLinha", nullable = false, unique = true)
    private Integer idTabelaCustoLinha;

    @Column(name = "PeriodoExecucaoTabelaCusto", nullable = false)
    private Integer periodoExecucaoTabelaCusto;

    @Column(name = "QuantidadeHorasTabelaCusto", nullable = false)
    private Integer quantidadeHorasTabelaCusto;

    @Column(name = "ValorHoraTabelaCusto", nullable = false)
    private Double valorHoraTabelaCusto;

    @JoinColumn(name = "IdTabelaCusto")
    @ManyToOne
    @JsonIgnore
    private TabelaCusto tabelaCusto;

    @Column(name = "perfilDespesaTabelaCustoLinha", nullable = false)
    String perfilDespesaTabelaCustoLinha;
}
