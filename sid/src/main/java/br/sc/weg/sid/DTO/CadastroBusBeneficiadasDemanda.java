package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.TamanhoDemanda;
import lombok.Data;

@Data
public class CadastroBusBeneficiadasDemanda {
    private String secaoTIResponsavel;
    private String buSolicitante;
    private String busBeneficiadasDemanda;
    private TamanhoDemanda tamanhoDemanda;
}
