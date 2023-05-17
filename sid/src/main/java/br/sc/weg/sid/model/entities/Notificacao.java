package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

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

    @Column(length = 300, name = "TextoNotificacao", nullable = false)
    private String textoNotificacao;

    @Column(name = "LinkNotificacao", nullable = false, length = 255)
    private String linkNotificacao;

    @Column(name = "tipoNotificacao", nullable = false)
    private String tipoNotificacao;

    @Column(name = "tempoNotificacao", nullable = false)
    private String tempoNotificacao;

    @Column(name = "responsavel", nullable = false)
    private String responsavel;

    @ManyToOne()
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;
}
