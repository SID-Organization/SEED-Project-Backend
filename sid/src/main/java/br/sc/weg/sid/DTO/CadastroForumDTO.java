package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
public class CadastroForumDTO {

    private String nomeForum;

    private Usuario analistaResponsavel;

    private List<Usuario> usuariosForum;
}
