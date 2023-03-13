package br.sc.weg.sid.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CadastroPdfDemandaDTO {

    @NotNull(message = "O campo idDemanda não pode ser nulo")
    public String propostaMelhoriaDemandaHTML;

    @NotNull(message = "O campo idDemanda não pode ser nulo")
    public String situacaoAtualDemandaHTML;

}
