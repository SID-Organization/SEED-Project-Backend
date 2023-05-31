package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Beneficio;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CadastroPdfDemandaDTO {

    @NotNull(message = "O campo propostaMelhoriaDemandaHTML não pode ser nulo")
    public String propostaMelhoriaDemandaHTML;

    @NotNull(message = "O campo situacaoAtualDemandaHTML não pode ser nulo")
    public String situacaoAtualDemandaHTML;

    @NotNull(message = "O campo frequenciaUsoDemandaHTML não pode ser nulo")
    public String frequenciaUsoDemandaHTML;

}
