package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Pauta;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CadastroAtaDTO {
    @NotNull(message = "O numero da DG não pode ser nulo")
    private Integer numeroDgAta;

    @NotNull(message = "O campo pautaAta não pode ser nulo")
    private Pauta pautaAta;
}
