package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Comissao;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CadastroForumDTO {

    @NotNull(message = "O campo analistaResponsavel não pode ser nulo")
    private Usuario analistaResponsavelForum;
    @NotNull(message = "O campo usuariosForum não pode ser nulo")
    private List<Usuario> usuariosForum;
    private Comissao comissaoForum;


}
