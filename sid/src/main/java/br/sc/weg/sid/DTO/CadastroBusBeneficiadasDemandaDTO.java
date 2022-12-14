package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.BusinessUnity;
import br.sc.weg.sid.model.entities.TamanhoDemanda;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CadastroBusBeneficiadasDemandaDTO {
    private List<BusinessUnity> busBeneficiadasDemanda;
}
