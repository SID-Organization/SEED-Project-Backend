package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "BU")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BusinessUnity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBusinessUnity", nullable = false, unique = true)
    private Integer idBusinessUnity;

    @Column(name = "nomeBusinessUnity", nullable = false)
    private String nomeBusinessUnity;
}
