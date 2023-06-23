package br.sc.weg.sid.DTO.filtro.demanda;

import lombok.Data;

@Data
public class FiltroDemandaDTO {

    private String filterBy;
    private Object value;
    private Object endValue;
    private String type;

}
