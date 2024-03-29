package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.model.enums.StatusDemanda;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class DemandaResumida {

    Integer idDemanda;

    String tituloDemanda;

    Date prazoElaboracaoDemanda;

    Double scoreDemanda;

    Double custoTotalDemanda;

    StatusDemanda statusDemanda;

    String nomeSolicitante;

    String nomesAnalistasResponsaveis;

    String nomeGerenteResponsavelDemanda;

    String forumDeAprovacaoDemanda;

    String departamentoDemanda;

    String tamanhoDemanda;

    Integer codigoPPMDemanda;

    String motivoRecusaDemanda;
}
