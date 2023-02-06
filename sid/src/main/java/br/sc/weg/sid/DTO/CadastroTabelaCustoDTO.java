package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.CentroCusto;
import br.sc.weg.sid.model.entities.PerfilDeDespesa;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.TipoDeDespesa;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    private TipoDeDespesa tipoDespesa;

    private PerfilDeDespesa perfilDespesa;

    private Integer periodoExecucao;

    private Integer quantidadeHorasNecessarias;

    private Double valorHora;

    private List<CentroCusto> centroCusto;
}

