package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class MensagemDTO {
    private String textoMensagem;

    private byte[] arquivoMensagem;

    private Date dataMensagem;

    private Usuario idUsuario;

    private Chat idChat;
}
