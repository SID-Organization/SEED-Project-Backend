package br.sc.weg.sid.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTabelaCusto", nullable = false, unique = true)
    private Integer idTabelaCusto;

    @Column(name = "TipoDespesa", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDeDespesa tipoDespesa;

    @OneToMany(mappedBy = "idTabelaCustoLinha")
    private List<TabelaCustoLinha> tabelaCustoLinha;

    @OneToMany(mappedBy = "idCentroCustoTabelaCusto")
    private List<CentroCustoTabelaCusto> centroCustoTabelaCusto;
}
