package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.enums.StatusDemanda;
import lombok.Data;

@Data
public class DevolverDemandaDTO {
    private String motivoRecusaDemanda;
    private StatusDemanda statusDemanda;
    private Usuario idResponsavel;
}
