package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Comissao;
import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.Proposta;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class AtualizaPautaDTO {
    private Date dataReuniaoPauta;
    private LocalTime horarioInicioPauta;
    private LocalTime horarioTerminoPauta;
    private Forum forumPauta;
    private List<Proposta> propostasPauta;
}
