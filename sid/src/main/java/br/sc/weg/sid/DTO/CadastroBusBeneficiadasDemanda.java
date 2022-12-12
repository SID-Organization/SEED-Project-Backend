package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.BusinessUnity;
import br.sc.weg.sid.model.entities.TamanhoDemanda;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CadastroBusBeneficiadasDemanda {
    private String secaoTIResponsavel;
    private String buSolicitante;
    private String busBeneficiadasDemanda;
    private TamanhoDemanda tamanhoDemanda;
}
