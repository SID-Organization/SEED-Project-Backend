package br.sc.weg.sid.model.entities;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class AtaResumida {

    Integer idAta;

    Integer qtdPropostas;

    Date dataReuniaoAta;

    LocalTime horarioInicioAta;

    String analistaResponsavel;

    List<PropostasLog> propostasLog;

    String numeroAtaPublicada;

    String numeroAtaNaoPublicada;

}
