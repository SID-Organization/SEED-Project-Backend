package br.sc.weg.sid.utils;

import br.sc.weg.sid.DTO.CadastroDemandaDTO;
import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.service.ArquivoDemandaService;
import br.sc.weg.sid.model.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@NoArgsConstructor
public class DemandaUtil {

    @Autowired
    UsuarioService usuarioService;

    private ObjectMapper mapper = new ObjectMapper();

    public Demanda convertDtoToModel(CadastroDemandaDTO cadastroDemandaDTO) {
        Demanda demanda = new Demanda();
        BeanUtils.copyProperties(cadastroDemandaDTO, demanda);
        return demanda;
    }

    public CadastroDemandaDTO convertToDto(String  demandaJson) {
        try{
            return this.mapper.readValue(demandaJson, CadastroDemandaDTO.class);
        } catch (Exception e){
            throw  new RuntimeException("Erro ao converter o demandaJson para objeto CadastroDemandaDTO! \n" + e.getMessage());
        }
    }
}
