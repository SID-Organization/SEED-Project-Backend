package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.*;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import java.util.Date;
import java.util.List;

@Data
public class CadastroDemandaDTO {

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

    public List<Beneficio> beneficiosDemanda;
}
