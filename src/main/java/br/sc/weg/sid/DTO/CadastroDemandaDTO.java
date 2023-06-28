package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Beneficio;
import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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

//    public Integer codigoPPMDemanda;

    public Usuario solicitanteDemanda;

    public Usuario gerenteDaAreaDemanda;

    public Usuario gestorResponsavelDemanda;

    public List<Beneficio> beneficiosDemanda;
}
