package br.sc.weg.sid.model.entities;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ChatResumido {
    private String tituloDemanda;
    private byte[] fotoAnalista;
    private String nomeAnalista;
    private String ultimaMensagem;
    private String dataUltimaMensagem;
}
