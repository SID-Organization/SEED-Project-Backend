package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.enums.StatusDemanda;
import lombok.Data;

import java.util.List;

@Data
public class RetornoHistoricoStatusDemandaDTO {
    private StatusDemanda status;
    private List<RetornoHistoricoStatusDemandaPayloadDTO> dados;
}
