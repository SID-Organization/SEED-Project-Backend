package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CadastroChatDTO {
    @NotNull(message = "O campo ativoChat não pode ser nulo")
    private Boolean ativoChat;
    @NotNull(message = "O campo idDemanda não pode ser nulo")
    private Demanda idDemanda;
    @NotNull(message = "O campo usuarios não pode ser nulo")
    private List<Usuario> usuarios;
}
