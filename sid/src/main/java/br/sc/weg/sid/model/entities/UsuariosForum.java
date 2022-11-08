package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USUARIOS_FORUM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UsuariosForum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuariosForum", nullable = false, unique = true)
    private Integer idUsuariosForum;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario idUsuario;

    @ManyToOne
    @JoinColumn(name = "idForum", nullable = false)
    private Forum idForum;
}
