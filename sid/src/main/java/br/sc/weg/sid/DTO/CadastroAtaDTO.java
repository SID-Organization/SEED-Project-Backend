package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.ParecerComissao;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.TipoAta;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
public class CadastroAtaDTO {
    @NotNull(message = "O numero da DG não pode ser nulo")
    private Integer numeroDgAta;

    @NotNull(message = "O campo pautaAta não pode ser nulo")
    private Pauta pautaAta;

    @NotNull(message = "O campo parecerComissao não pode ser nulo")
    private ParecerComissao parecerComissaoPropostaLog;

    @NotNull(message = "O campo consideracoesProposta não pode ser nulo")
    private String consideracoesProposta;

    @NotNull(message = "O campo tipoAta não pode ser nulo")
    private TipoAta tipoAta;
}
