package br.sc.weg.sid.model.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
public class DemandaResumida {

    private Integer idDemanda;
    private String tituloDemanda;
}
