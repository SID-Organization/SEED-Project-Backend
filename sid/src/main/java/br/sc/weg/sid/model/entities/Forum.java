package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FORUM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Forum {

    @Id
    @Column(name = "idForum", nullable = false, unique = true)
    private Integer idForum;

    @Column(name = "nomeForum", nullable = false)
    private String nomeForum;
}
