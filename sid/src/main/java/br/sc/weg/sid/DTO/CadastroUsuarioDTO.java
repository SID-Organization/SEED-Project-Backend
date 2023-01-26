package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Cargo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CadastroUsuarioDTO {
    @NotNull(message = "O campo numeroCadastroUsuario é obrigatório")
    private Integer numeroCadastroUsuario;
    @NotBlank(message = "O campo nomeUsuario é obrigatório")
    private String nomeUsuario;
    @NotBlank(message = "O campo departamentoUsuario é obrigatório")
    private String departamentoUsuario;
    @NotBlank(message = "O campo businessUnity é obrigatório")
    private String businessUnity;
    @NotNull(message = "O campo cargoUsuario é obrigatório")
    private Cargo cargoUsuario;
    @NotBlank(message = "O campo emailUsuario é obrigatório")
    private String emailUsuario;
    @NotBlank(message = "O campo senhaUsuario é obrigatório")
    private String senhaUsuario;
}
