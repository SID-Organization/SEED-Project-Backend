package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.*;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CadastroDemandaDTO {

    private String tituloDemanda;

    private String propostaDemanda;

    private String objetivoDemanda;

    private String situacaoAtualDemanda;

    private Integer frequenciaUsoDemanda;

    private String descricaoQualitativoDemanda;

    @FutureOrPresent
    private Date prazoElaboracaoDemanda;

    private Integer solicitanteDemanda;

    private Chat idChat;

    private List<BusinessUnity> busBeneficiadas;

    private List<Beneficio> beneficios;
}
