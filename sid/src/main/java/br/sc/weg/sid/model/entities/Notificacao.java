package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "NOTIFICACAO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdNotificacao", nullable = false, unique = true)
    private Integer idNotificacao;

    @Column(length = 60, name = "TextoNotificacao", nullable = false)
    private String textoNotificacao;

    @Column(name = "LinkNotificacao", nullable = false, length = 255)
    private String linkNotificacao;

    @ManyToMany
    @JoinTable(name = "NOTIFICACAO_USUARIO",
            joinColumns = @JoinColumn(name = "IdNotificacao"),
            inverseJoinColumns = @JoinColumn(name = "IdUsuario"))
    private List<Usuario> usuarios;
}
