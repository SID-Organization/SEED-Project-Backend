package br.sc.weg.sid.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CadastroBusinessUnityDTO {
    @NotNull(message = "O campo nomeBusinessUnity não pode ser nulo")
    private String nomeBusinessUnity;
}
