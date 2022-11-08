package br.sc.weg.sid.utils;

import br.sc.weg.sid.DTO.CadastroDemandaDTO;
import br.sc.weg.sid.model.entities.Demanda;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DemandaUtil {

    private ObjectMapper mapper = new ObjectMapper();

    public Demanda convertJsonToModel(String demandaJson) {
        try {
            CadastroDemandaDTO cadastroDemandaDTO = convertToDto(demandaJson);
            return convertDtoToModel(cadastroDemandaDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o itemJson para objeto Item");
        }
    }

    private Demanda convertDtoToModel(CadastroDemandaDTO cadastroDemandaDTO) {
        return this.mapper.convertValue(cadastroDemandaDTO, Demanda.class);
    }

    public CadastroDemandaDTO convertToDto(String  demandaJson) {
        try{
            return this.mapper.readValue(demandaJson, CadastroDemandaDTO.class);
        } catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
