package br.sc.weg.sid.security.DTO;

import lombok.Data;
import lombok.NonNull;

@Data
public class UsuarioDTO {
    @NonNull
    private Integer numeroCadastroUsuario;
    @NonNull
    private String senhaUsuario;
}
