package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Proposta;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CadastroTabelaCustoDTO {
    @NotBlank(message = "O campo Nome da Tabela de Custo é obrigatório")
    private String nomeTabelaCusto;
    @NotNull(message = "O campo Licenças é obrigatório")
    private Integer licensasTabelaCusto;
    @NotNull(message = "O campo Esforço é obrigatório")
    private Integer esforcoTabelaCusto;
    @NotNull(message = "O campo Total é obrigatório")
    private Double totalTabelaCusto;
    @NotNull(message = "O campo Proposta é obrigatório")
    private Proposta idProposta;
}

