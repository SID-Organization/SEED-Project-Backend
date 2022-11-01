package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "BU")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Bu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IdBu", nullable = false, unique = true)
    private Integer idBu;

    @Column(name = "NomeBu", nullable = false)
    private String nomeBu;
}
