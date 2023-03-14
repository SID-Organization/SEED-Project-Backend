package br.sc.weg.sid.model.entities;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class AtaResumida {

    Integer idAta;

    Integer qtdPropostas;

    Date dataReuniaoAta;

    LocalTime horarioInicioAta;

    Usuario analistaResponsavel;

}
