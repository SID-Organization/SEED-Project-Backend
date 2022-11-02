package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USUARIOS_COMISSAO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UsuariosComissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuariosComissao", nullable = false, unique = true)
    private Integer idUsuariosComissao;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario idUsuario;

    @ManyToOne
    @JoinColumn(name = "idComissao", nullable = false)
    private Comissao idComissao;
}
