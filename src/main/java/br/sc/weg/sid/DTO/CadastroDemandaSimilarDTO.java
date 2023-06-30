package br.sc.weg.sid.DTO;

import lombok.Data;

@Data
public class CadastroDemandaSimilarDTO {
    private int idDemanda;
    private String tituloDemanda;
    private String propostaMelhoriaDemanda;
    private String descricaoQualitativoDemanda;
    private String frequenciaUsoDemanda;
    private String situacaoAtualDemanda;
}
