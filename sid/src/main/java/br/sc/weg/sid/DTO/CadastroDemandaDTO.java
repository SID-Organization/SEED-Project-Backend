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
    @NotBlank(message = "O campo propostaMelhoriaDemanda não pode estar em branco!")
    public String propostaMelhoriaDemanda;
    @NotBlank(message = "O campo situacaoAtualDemanda não pode estar em branco!")
    public String situacaoAtualDemanda;
    @NotBlank(message = "O campo descricaoQualitativoDemanda não pode estar em branco!")
    public String descricaoQualitativoDemanda;

//    public String secaoSolicitanteDemanda;

//    public TamanhoDemanda tamanhoDemanda;

    @NotBlank(message = "O campo frequenciaUsoDemanda não pode estar em branco!")
    public String frequenciaUsoDemanda;

//    @FutureOrPresent
//    public Date prazoElaboracaoDemanda;

//    public Integer codigoPPMDemanda;

    @NotNull(message = "O campo solicitanteDemanda não pode ser nulo")
    public Usuario solicitanteDemanda;
    @NotNull(message = "O campo analistaResponsavelDemanda não pode ser nulo")
    public Usuario analistaResponsavelDemanda;
    @NotNull(message = "O campo beneficiosDemanda não pode ser nulo")
    public List<Beneficio> beneficiosDemanda;
}
