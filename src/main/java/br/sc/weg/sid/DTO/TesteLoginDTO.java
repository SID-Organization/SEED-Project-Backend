package br.sc.weg.sid.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TesteLoginDTO {
    @NotNull(message = "O campo numeroCadastroUsuario é obrigatório")
    Integer numeroCadastroUsuario;
    @NotBlank(message = "O campo senhaUsuario é obrigatório")
    String senhaUsuario;
}
