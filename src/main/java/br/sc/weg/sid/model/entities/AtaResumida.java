package br.sc.weg.sid.model.entities;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class AtaResumida {

    Integer idAta;

    String nomeForumAta;

    Integer qtdPropostas;

    Date dataReuniaoAta;

    LocalTime horarioInicioAta;

    LocalTime horarioTerminoAta;

    String analistaResponsavel;

    List<PropostasLog> propostasLog;


}
