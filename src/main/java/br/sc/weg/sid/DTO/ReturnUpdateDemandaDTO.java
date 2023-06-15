package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.ArquivoDemanda;
import br.sc.weg.sid.model.entities.Demanda;
import lombok.Data;

import java.util.List;

@Data
public class ReturnUpdateDemandaDTO {

    private Demanda demanda;
    private List<ArquivoDemanda> arquivosDemanda;
}
