package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Cargo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CadastroUsuarioDTO {
    private Integer numeroCadastroUsuario;
    private String nomeUsuario;
    private String departamentoUsuario;
    private Cargo cargoUsuario;
    private String emailUsuario;
    private String senhaUsuario;
}
