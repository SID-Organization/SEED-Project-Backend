package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.ParecerComissao;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.TipoAta;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CadastroAtaDTO {
    @NotNull(message = "O numero da DG não pode ser nulo")
    private Integer numeroDgAta;

    @NotNull(message = "O campo pautaAt a não pode ser nulo")
    private Pauta pautaAta;
    @NotNull(message = "A lista de PropostaLogDTO não pode ser nulo")
    private List<CadastroPropostaLogDTO> propostasLogDTO;

}
