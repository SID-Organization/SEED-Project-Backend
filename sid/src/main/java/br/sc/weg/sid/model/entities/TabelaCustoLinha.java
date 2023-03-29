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

    @Column(name = "PeriodoExecucaoTabelaCusto")
    private Integer periodoExecucaoTabelaCusto;

    @Column(name = "QuantidadeHorasTabelaCusto")
    private Integer quantidadeHorasTabelaCusto;

    @Column(name = "ValorHoraTabelaCusto")
    private Double valorHoraTabelaCusto;

    @Column(name = "perfilDespesaTabelaCustoLinha")
    String perfilDespesaTabelaCustoLinha;
}
