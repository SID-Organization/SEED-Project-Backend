package br.sc.weg.sid.model.entities;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class MensagemResumida {
    String textoMensagem;
    Integer idUsuario;
    String dataMensagem;
    Integer idChat;
    Integer idMensagem;
    Object arquivoMensagem;
}
