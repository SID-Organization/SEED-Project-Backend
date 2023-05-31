package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MENSAGEM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IdMensagem", nullable = false, unique = true)
    private Integer idMensagem;

    @Column(name = "TextoMensagem", nullable = false)
    private String textoMensagem;

    @Column(name = "ArquivoMensagem")
    private byte[] arquivoMensagem;

    @Column(name = "DataMensagem", nullable = false)
    private Date dataMensagem;

    @JoinColumn(name = "idUsuario", nullable = false)
    @ManyToOne
    private Usuario idUsuario;

    @JoinColumn(name = "idChat", nullable = false)
    @ManyToOne
    private Chat idChat;
}
