package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.model.enums.Cargo;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "USUARIO")
@Data
@AllArgsConstructor @NoArgsConstructor
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

    @OneToOne
    @JoinColumn(name = "DepartamentoUsuario", nullable = false)
    private BusinessUnity departamentoUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "CargoUsuario", nullable = false)
    private Cargo cargoUsuario;

    @Column(name = "BusinessUnity", nullable = false)
    private String businessUnity;

    @Column(name = "FotoUsuario")
    @Lob
    private byte[] fotoUsuario;

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = new BCryptPasswordEncoder().encode(senhaUsuario);
    }
}
