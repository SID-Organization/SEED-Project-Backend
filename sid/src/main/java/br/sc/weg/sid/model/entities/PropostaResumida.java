package br.sc.weg.sid.model.entities;

import lombok.Data;

import java.util.List;

@Data
public class PropostaResumida {

    Integer idProposta;

    String demandaPropostaTitulo;

    Integer tempoDeExecucaoDemanda;

    Double valorDemanda;

    Integer idDemanda;

    String linkPropostaJira;

    List<TabelaCustoLinha> tabelaCustoLinha;
}
