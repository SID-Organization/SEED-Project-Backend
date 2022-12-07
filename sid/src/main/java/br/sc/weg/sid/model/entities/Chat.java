package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CHAT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdChat", nullable = false, unique = true)
    private Integer idChat;

    @Column(name = "AtivoChat", nullable = false)
    private Integer ativoChat;

    @OneToOne
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda idDemanda;

    @ManyToMany
    @JoinTable(name = "USUARIO_CHAT",
            joinColumns = @JoinColumn(name = "idChat"),
            inverseJoinColumns = @JoinColumn(name = "idUsuario"))
    private List<Usuario> usuarios;
}
