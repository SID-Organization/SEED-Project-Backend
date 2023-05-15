package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.BusinessUnity;
import br.sc.weg.sid.model.entities.SecaoTIResponsavel;
import br.sc.weg.sid.model.enums.TamanhoDemanda;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CadastroBusBeneficiadasDemandaDTO {
    @NotNull(message = "O campo secaoTIResponsavelDemanda é obrigatório")
    private SecaoTIResponsavel secaoTIResponsavelDemanda;
    @NotNull(message = "O campo busBeneficiadasDemanda não pode ser nulo")
    private List<BusinessUnity> busBeneficiadasDemanda;
    @NotNull(message = "O campo tamanhoDemanda não pode ser nulo")
    private TamanhoDemanda tamanhoDemanda;
}
