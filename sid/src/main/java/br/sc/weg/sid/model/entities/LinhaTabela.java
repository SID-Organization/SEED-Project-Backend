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
    @Column(name = "IdLinhaTabela", nullable = false, unique = true)
    private Integer idLinhaTabela;

    @Column(name = "NomeLinhaTabela", nullable = false)
    private String nomeLinhaTabela;

    @Column(name = "licencaLinhaTabela")
    private Integer licencaLinhaTabela;

    @Column(name = "esforcoLinhaTabela")
    private Integer esforcoLinhaTabela;

    @Column(name = "totalLinhaTabela", nullable = false)
    private Double totalLinhaTabela;

    @ManyToOne
    @JoinColumn(name = "idTabelaCusto", nullable = false)
    private TabelaCusto idTabelaCusto;
}
