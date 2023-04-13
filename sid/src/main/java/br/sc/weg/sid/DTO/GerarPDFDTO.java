package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Proposta;
import lombok.Data;
@Data
public class GerarPDFDTO {
    Integer idDemanda;
    Proposta proposta;
}
