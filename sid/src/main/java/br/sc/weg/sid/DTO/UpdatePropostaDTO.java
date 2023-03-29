package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.TabelaCusto;
import br.sc.weg.sid.model.entities.TabelaCustoLinha;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UpdatePropostaDTO {

    private String escopoProposta;
    private Double paybackProposta;
    private Integer aprovadoWorkflowProposta;
    private Date periodoExecucaoDemandaInicio;
    private Date periodoExecucaoDemandaFim;
    private String naoFazParteDoEscopoProposta;
    private String alternativasAvaliadasProposta;
    private String planoMitigacaoProposta;
    private Double custosTotaisDoProjeto;
    private Double custosInternosDoProjeto;
    private Double custosExternosDoProjeto;
    private TabelaCusto tabelaCustoExterno;
    private TabelaCusto tabelaCustoInterno;
    private String nomeResponsavelNegocio;
    private String areaResponsavelNegocio;


}
