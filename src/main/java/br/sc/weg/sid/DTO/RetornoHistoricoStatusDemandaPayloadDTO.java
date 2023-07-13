package br.sc.weg.sid.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class RetornoHistoricoStatusDemandaPayloadDTO {
    private String data;
    private Integer quantidade;
}
