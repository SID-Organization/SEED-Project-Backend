package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USUARIOS_NO_CHAT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UsuariosChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuariosNoChat", nullable = false, unique = true)
    private Integer idUsuariosNoChat;

    @ManyToOne
    @JoinColumn(name = "idChat", nullable = false)
    private Chat idChat;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario idUsuario;

}
