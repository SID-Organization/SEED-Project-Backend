package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.*;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import java.util.Date;
import java.util.List;

@Data
public class CadastroDemandaDTO {

    public String tituloDemanda;

    public String propostaDemanda;

    public String situacaoAtualDemanda;

    public String buSolicitanteDemanda;

    public String secaoSolicitanteDemanda;

    public TamanhoDemanda tamanhoDemanda;

    public String frequenciaUsoDemanda;

    public String descricaoQualitativoDemanda;

    @FutureOrPresent
    public Date prazoElaboracaoDemanda;

    public Integer codigoPPM;

    public Usuario solicitanteDemanda;

    public List<BusinessUnity> busBeneficiadas;

    public List<Beneficio> beneficiosDemanda;
}
