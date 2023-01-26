package br.sc.weg.sid.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CadastroAtaDTO {
    @NotNull(message = "O numero da DG não pode ser nulo")
    private Integer numeroDgAta;
}
