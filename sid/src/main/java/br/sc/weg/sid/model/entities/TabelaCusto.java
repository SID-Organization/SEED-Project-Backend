package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TABELA_CUSTO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class TabelaCusto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTabelaCusto", nullable = false, unique = true)
    private Integer idTabelaCusto;

    @Column(name = "NomeTabelaCusto", nullable = false)
    private String nomeTabelaCusto;

    @Column(name = "TipoDespesa", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDeDespesa tipoDespesa;

    @Column(name = "PerfilDespesa", nullable = false)
    @Enumerated(EnumType.STRING)
    private PerfilDeDespesa perfilDespesa;

    @Column(name = "PeriodoExecucao", nullable = false)
    private Integer periodoExecucao;

    @Column(name = "QuantidadeHorasNecessarias", nullable = false)
    private Integer quantidadeHorasNecessarias;

    @Column(name = "ValorHora", nullable = false)
    private Double valorHora;

    @Column(name = "totalTabelaCusto", nullable = false)
    private Double totalTabelaCusto;

    @ManyToMany
    @JoinTable(name = "centro_custo_tabela_custo",
            joinColumns = @JoinColumn(name = "idTabelaCusto"),
            inverseJoinColumns = @JoinColumn(name = "idCentroCusto"))
    private List<CentroCusto> centroCusto;

    @OneToOne
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta idProposta;
}
