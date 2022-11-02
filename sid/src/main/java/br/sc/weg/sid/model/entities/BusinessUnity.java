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
public class BusinessUnity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IdBusinessUnity", nullable = false, unique = true)
    private Integer idBusinessUnity;

    @Column(name = "NomeBusinessUnity", nullable = false)
    private String nomeBusinessUnity;
}
