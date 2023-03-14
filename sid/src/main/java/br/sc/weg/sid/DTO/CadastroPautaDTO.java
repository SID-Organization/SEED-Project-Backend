package br.sc.weg.sid.DTO;


import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class CadastroPautaDTO {
    @FutureOrPresent(message = "A data de inicio não pode ser no passado")
    private Date dataReuniaoPauta;
    @NotNull(message = "O campo forumPauta não pode ser nulo")
    private Forum forumPauta;
    @NotNull(message = "O campo propostasPauta não pode ser nulo")
    private List<Proposta> propostasPauta;

    @NotNull(message = "O campo horarioInicioPauta não pode ser nulo")
    private LocalTime horarioInicioPauta;

    @NotNull(message = "O campo horarioTerminoPauta não pode ser nulo")
    private LocalTime horarioTerminoPauta;

    @NotNull(message = "O campo analistaResponsavelPauta não pode ser nulo")
    private Usuario analistaResponsavelPauta;

}
