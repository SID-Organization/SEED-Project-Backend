package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "LINHA_TABELA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LinhaTabela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdLinhaTabela", nullable = false, unique = true)
    private Integer idLinhaTabela;

    @Column(name = "PerfilDespesaLinhaTabela", nullable = false)
    private String perfilDespesaLinhaTabela;

    @Column(name = "PeriodoExecucaoLinhaTabela", nullable = false)
    private Integer periodoExecucaoLinhaTabela;

    @Column(name = "QuantidadeHorasLinhaTabela", nullable = false)
    private Integer quantidadeHorasLinhaTabela;

    @Column(name = "ValorHoraLinhaTabela", nullable = false)
    private Double valorHoraLinhaTabela;

    @Column(name = "ValorTotalLinhaTabela", nullable = false)
    private Double valorTotalLinhaTabela;

    @OneToOne
    @JoinColumn(name = "IdTabelaCusto", referencedColumnName = "IdTabelaCusto")
    private TabelaCusto tabelaCusto;
}
