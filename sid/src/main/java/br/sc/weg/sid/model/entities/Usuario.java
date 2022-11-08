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
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IdUsuario", nullable = false, unique = true)
    private Integer idUsuario;

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

    @Column(name = "CargoUsuario", nullable = false)
    private Cargo cargoUsuario;

    @Column(name = "FotoUsuario", nullable = false)
    @Lob
    @NonNull
    private byte[] fotoUsuario;
}
