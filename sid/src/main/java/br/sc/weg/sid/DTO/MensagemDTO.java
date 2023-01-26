package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MensagemDTO {
    @NotBlank(message = "O campo textoMensagem não pode estar em branco!")
    private String textoMensagem;

    private byte[] arquivoMensagem;

    @NotNull(message = "O campo dataMensagem não pode ser nulo")
    private Date dataMensagem;
    @NotNull(message = "O campo idUsuario não pode ser nulo")
    private Usuario idUsuario;
    @NotNull(message = "O campo idChat não pode ser nulo")
    private Chat idChat;
}
