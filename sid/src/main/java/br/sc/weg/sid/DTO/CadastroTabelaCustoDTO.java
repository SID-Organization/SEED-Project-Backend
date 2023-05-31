package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.CentroCusto;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.enums.TipoDeDespesa;
import lombok.Data;
import lombok.NonNull;

@Data
public class CadastroTabelaCustoDTO {

    @NonNull
    private TipoDeDespesa tipoDespesa;
    @NonNull
    private String perfilDespesaTabelaCusto;
    @NonNull
    private Integer periodoExecucaoTabelaCusto;
    @NonNull
    private Integer quantidadeHorasTabelaCusto;
    @NonNull
    private Double valorHoraTabelaCusto;
    @NonNull
    private Proposta proposta;
    @NonNull
    private CentroCusto centroCusto;
}

