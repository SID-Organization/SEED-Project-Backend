package br.sc.weg.sid.auth.DTO;

import lombok.Data;
import lombok.NonNull;

@Data
public class TokenDTO {
    @NonNull
    private String tipo;
    @NonNull
    private String token;
}
