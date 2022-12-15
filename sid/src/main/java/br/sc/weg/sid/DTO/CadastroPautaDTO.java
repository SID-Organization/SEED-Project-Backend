package br.sc.weg.sid.DTO;


import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.Proposta;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import java.util.Date;
import java.util.List;

@Data
public class CadastroPautaDTO {
    @FutureOrPresent
    private Date dataReuniaoPauta;

    private Forum forumPauta;

    private List<Proposta> propostasPauta;
}
