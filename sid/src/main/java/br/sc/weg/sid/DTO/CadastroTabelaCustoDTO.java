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

    private TipoDeDespesa tipoDespesa;

    private String perfilDespesaTabelaCusto;

    private Integer periodoExecucaoTabelaCusto;

    private Integer quantidadeHorasTabelaCusto;

    private Double valorHoraTabelaCusto;

    private Proposta proposta;

    private CentroCusto centroCusto;
}

