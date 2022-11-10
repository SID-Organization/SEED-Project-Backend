package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USUARIO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Usuario {

    @Id
    @Column(name = "NumeroCadastroUsuario", nullable = false)
    private Integer numeroCadastroUsuario;

    @Column(name = "NomeUsuario", nullable = false)
    private String nomeUsuario;

    @Column(name = "EmailUsuario", nullable = false)
    private String emailUsuario;

    @Column(name = "SenhaUsuario", nullable = false)
    private String senhaUsuario;

    @Column(name = "DepartamentoUsuario", nullable = false)
    private String departamentoUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "CargoUsuario", nullable = false)
    private Cargo cargoUsuario;

    @Column(name = "FotoUsuario", nullable = false)
    @Lob
    @NonNull
    private byte[] fotoUsuario;
}
