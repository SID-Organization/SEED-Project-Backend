package br.sc.weg.sid.utils;

import br.sc.weg.sid.DTO.CadastroHistoricoWorkflowDTO;
import br.sc.weg.sid.DTO.CadastroUsuarioDTO;
import br.sc.weg.sid.model.entities.HistoricoWorkflow;
import br.sc.weg.sid.model.entities.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HistoricoWorkflowUtil {
    private ObjectMapper mapper = new ObjectMapper();

    public HistoricoWorkflow convertJsonToModel(String historicoWorkflowJson) {
        try {
            CadastroHistoricoWorkflowDTO cadastroHistoricoWorkflowDTO = convertToDto(historicoWorkflowJson);
            return convertDtoToModel(cadastroHistoricoWorkflowDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o itemJson para objeto Item! \n" + e.getMessage());
        }
    }

    private HistoricoWorkflow convertDtoToModel(CadastroHistoricoWorkflowDTO cadastroHistoricoWorkflowDTO) {
        return this.mapper.convertValue(cadastroHistoricoWorkflowDTO, HistoricoWorkflow.class);
    }

    public CadastroHistoricoWorkflowDTO convertToDto(String historicoWorkflowJson) {
        try{
            return this.mapper.readValue(historicoWorkflowJson, CadastroHistoricoWorkflowDTO.class);
        } catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
