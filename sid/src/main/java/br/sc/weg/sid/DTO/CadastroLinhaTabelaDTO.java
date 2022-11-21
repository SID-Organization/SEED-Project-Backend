package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.TabelaCusto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CadastroLinhaTabelaDTO {
    @NotBlank(message = "Nome da linha da tabela de custo é obrigatório")
    private String nomeLinhaTabela;
    @NotNull(message = "Licença da linha da tabela de custo é obrigatório")
    private Integer licencaLinhaTabela;
    @NotNull(message = "Esforço da linha da tabela de custo é obrigatório")
    private Integer esforcoLinhaTabela;
    @NotNull(message = "Total da linha da tabela de custo é obrigatório")
    private Double totalLinhaTabela;
    @NotNull(message = "Tabela de custo é obrigatório")
    private TabelaCusto idTabelaCusto;
}
