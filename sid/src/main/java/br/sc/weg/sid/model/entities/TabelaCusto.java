package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TABELA_CUSTO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TabelaCusto {
    @Id
    @Column(name = "IdTabelaCusto", nullable = false, unique = true)
    private Integer idTabelaCusto;

    @Column(name = "NomeTabelaCusto", nullable = false)
    private String nomeTabelaCusto;

    @Column(name = "licensasTabelaCusto", nullable = false)
    private Integer licensasTabelaCusto;

    @Column(name = "esforcoTabelaCusto", nullable = false)
    private Integer esforcoTabelaCusto;

    @Column(name = "totalTabelaCusto", nullable = false)
    private Double totalTabelaCusto;

    @OneToOne
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta idProposta;
}
