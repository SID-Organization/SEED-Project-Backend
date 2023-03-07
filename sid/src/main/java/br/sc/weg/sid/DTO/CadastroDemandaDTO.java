package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CadastroDemandaDTO {
    @NotBlank(message = "O campo tituloDemanda não pode estar em branco!")
    public String tituloDemanda;

    public String propostaMelhoriaDemanda;

    public String situacaoAtualDemanda;

    public String descricaoQualitativoDemanda;

//    public String secaoSolicitanteDemanda;

//    public TamanhoDemanda tamanhoDemanda;

    public String frequenciaUsoDemanda;

//    @FutureOrPresent
//    public Date prazoElaboracaoDemanda;

//    public Integer codigoPPMDemanda;

    public Usuario solicitanteDemanda;

    public Usuario analistaResponsavelDemanda;

    public Usuario gerenteDaAreaDemanda;

    public Usuario gestorResponsavelDemanda;

    public List<Beneficio> beneficiosDemanda;
}
