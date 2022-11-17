package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Usuario;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class CadastroForumDTO {

    private String nomeForum;

    private Usuario numeroCadastroAnalistaResponsavel;

    private List<Usuario> usuariosForum;
}
