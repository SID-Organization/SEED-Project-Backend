package br.sc.weg.sid.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Comissao")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idComissao", nullable = false, unique = true)
    private Integer idComissao;

    @Column(name = "nomeComissao", nullable = false)
    private String nomeComissao;

    @Column(name = "siglaComissao", nullable = false)
    private String siglaComissao;

}
