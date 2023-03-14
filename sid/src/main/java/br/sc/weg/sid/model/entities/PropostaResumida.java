package br.sc.weg.sid.model.entities;

import lombok.Data;

@Data
public class PropostaResumida {

    Integer idProposta;

    String demandaPropostaTitulo;

    Integer tempoDeExecucaoDemanda;

    Integer valorDemanda;

    Integer idDemanda;

    String linkPropostaJira;
}