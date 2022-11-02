package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMISSAO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Comissao {

    @Id
    @Column(name = "IdComissao", nullable = false, unique = true)
    private Integer idComissao;

    @Column(name = "NomeComissao", nullable = false)
    private String nomeComissao;
}
