package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class CadastroChatDTO {
    private Boolean ativoChat;
    private Demanda idDemanda;
    private List<Usuario> usuarios;
}
