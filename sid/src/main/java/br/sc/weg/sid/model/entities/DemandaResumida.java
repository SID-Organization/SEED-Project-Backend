package br.sc.weg.sid.model.entities;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class DemandaResumida {
    String tituloDemanda;

    Date prazoElaboracaoDemanda;

    Double scoreDemanda;

    StatusDemanda statusDemanda;
}
