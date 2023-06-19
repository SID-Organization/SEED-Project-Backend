package br.sc.weg.sid.model.entities;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class PautaResumida {

    Integer idPauta;

    Integer qtdPropostas;

    Date dataReuniao;

    LocalTime horaReuniao;

    String analistaResponsavel;

    LocalTime horaTerminoReuniao;

    List<PropostaResumida> propostaResumidas;
}
