package br.sc.weg.sid.model.entities;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "NOTIFICACOES_USUARIO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class NotificacoesUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNotificacoesUsuario", nullable = false, unique = true)
    private Integer idNotificacoesUsuario;

    @ManyToOne
    @JoinColumn(name = "idNotificacao", nullable = false)
    private Notificacao idNotificacoes;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario idUsuario;
}
