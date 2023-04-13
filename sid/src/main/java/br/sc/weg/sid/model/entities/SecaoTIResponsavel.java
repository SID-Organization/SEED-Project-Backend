package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "SecaoTIResponsavel")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecaoTIResponsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSecaoTIResponsavel", nullable = false, unique = true)
    private Integer idSecaoTIResponsavel;

    @Column(name = "nomeSecaoTIResponsavel", nullable = false)
    private String nomeSecaoTIResponsavel;

    @Column(name = "siglaSecaoTIResponsavel", nullable = false)
    private String siglaSecaoTIResponsavel;

}
