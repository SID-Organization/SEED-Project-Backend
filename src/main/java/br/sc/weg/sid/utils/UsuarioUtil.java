package br.sc.weg.sid.utils;

import br.sc.weg.sid.DTO.CadastroUsuarioDTO;
import br.sc.weg.sid.model.entities.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsuarioUtil {
    private ObjectMapper mapper = new ObjectMapper();

    public Usuario convertJsonToModel(String usuarioJson) {
        try {
            CadastroUsuarioDTO cadastroUsuarioDTO = convertToDto(usuarioJson);
            return convertDtoToModel(cadastroUsuarioDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o itemJson para objeto Item! \n" + e.getMessage());
        }
    }

    private Usuario convertDtoToModel(CadastroUsuarioDTO cadastroUsuarioDTO) {
        return this.mapper.convertValue(cadastroUsuarioDTO, Usuario.class);
    }

    public CadastroUsuarioDTO convertToDto(String usuarioJson) {
        try{
            return this.mapper.readValue(usuarioJson, CadastroUsuarioDTO.class);
        } catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
