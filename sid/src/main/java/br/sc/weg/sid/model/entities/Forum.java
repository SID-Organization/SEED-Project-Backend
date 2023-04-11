package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FORUM")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idForum", nullable = false, unique = true)
    private Integer idForum;

    @Column(name = "nomeForum", nullable = false)
    private String nomeForum;

    @Column(name = "siglaForum", nullable = false)
    private String siglaForum;

    @ManyToOne
    @JoinColumn(name = "numeroCadastroAnalistaResponsavel", nullable = false)
    private Usuario analistaResponsavelForum;

    @ManyToMany()
    @JoinTable(name = "usuarios_forum",
            joinColumns = @JoinColumn(name = "idForum"),
            inverseJoinColumns = @JoinColumn(name = "idUsuario"))
    private List<Usuario> usuariosForum;
}
