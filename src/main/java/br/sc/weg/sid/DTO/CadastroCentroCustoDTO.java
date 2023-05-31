package br.sc.weg.sid.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CadastroCentroCustoDTO {
    @NotNull(message = "O campo numeroCentroCusto não pode ser nulo")
    private String numeroCentroCusto;

    @NotNull(message = "O campo nomeCentroCusto não pode ser nulo")
    private String nomeCentroCusto;
}
