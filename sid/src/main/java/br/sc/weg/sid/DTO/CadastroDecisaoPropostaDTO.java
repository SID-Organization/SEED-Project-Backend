package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.Status;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
public class CadastroDecisaoPropostaDTO {
    @NotNull(message = "Proposta não pode ser nulo")
    private Proposta idProposta;
    @NotNull(message = "Pauta não pode ser nula")
    private Pauta idPauta;
    @NotNull(message = "O campo Ata não pode ser nulo")
    private Ata idAta;
    @NotNull(message = "Status da decisão da proposta não pode ser nulo")
    private Status statusDecisaoProposta;
    @NotNull(message = "O campo que define se a ata vai ser publicada ou não, não pode ser nulo")
    @Min(value = 1, message = "O campo que define se a ata vai ser publicada ou não, deve ser 1 ou 2")
    @Max(value = 2, message = "O campo que define se a ata vai ser publicada ou não, deve ser 1 ou 2")
    private Integer ataPublicadaDecisaoProposta;
    @NotBlank(message = "O campo Comentário é obrigatório")
    private String comentarioDecisaoProposta;
    @NotNull(message = "O campo Número Sequencial é obrigatório")
    @Min(value = 1, message = "O campo Número Sequencial deve ser maior que 0")
    private Integer numeroSequencialDecisaoProposta;
}
