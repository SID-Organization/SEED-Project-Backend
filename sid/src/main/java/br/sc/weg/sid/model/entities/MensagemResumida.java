package br.sc.weg.sid.model.entities;

import lombok.Data;

import java.util.Date;

@Data
public class MensagemResumida {
    String textoMensagem;
    Integer idUsuario;
    String dataMensagem;
    Integer idChat;
    Integer idMensagem;
}
