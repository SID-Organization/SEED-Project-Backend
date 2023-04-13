package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.BusinessUnity;
import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.SecaoTIResponsavel;
import br.sc.weg.sid.model.entities.TamanhoDemanda;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CadastroBusBeneficiadasDemandaDTO {
    @NotBlank(message = "O campo secaoTIResponsavelDemanda é obrigatório")
    private SecaoTIResponsavel secaoTIResponsavelDemanda;
    @NotBlank(message = "O campo buSolicitanteDemanda não pode estar em branco!")
    private BusinessUnity buSolicitanteDemanda;
    @NotNull(message = "O campo busBeneficiadasDemanda não pode ser nulo")
    private List<BusinessUnity> busBeneficiadasDemanda;
    @NotNull(message = "O campo tamanhoDemanda não pode ser nulo")
    private TamanhoDemanda tamanhoDemanda;
}
