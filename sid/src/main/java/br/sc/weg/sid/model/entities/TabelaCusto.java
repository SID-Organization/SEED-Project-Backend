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

    @Column(name = "TipoDespesa", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDeDespesa tipoDespesa;

    @Column(name = "totalTabelaCusto", nullable = false)
    private Double totalTabelaCusto;

//    @JoinColumn(name = "IdLinhaTabela", referencedColumnName = "IdLinhaTabela")
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<LinhaTabela> linhaTabela;
}
