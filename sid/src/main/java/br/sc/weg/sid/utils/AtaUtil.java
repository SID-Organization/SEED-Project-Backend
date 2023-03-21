package br.sc.weg.sid.utils;

import br.sc.weg.sid.DTO.CadastroAtaDTO;
import br.sc.weg.sid.DTO.CadastroDemandaDTO;
import br.sc.weg.sid.DTO.CadastroUsuarioDTO;
import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.AtaResumida;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class AtaUtil {

    @Autowired
    UsuarioService usuarioService;

    private ObjectMapper mapper = new ObjectMapper();

    public Ata convertJsonToModel(String ataJson) {
        try {
            CadastroAtaDTO cadastroAtaDTO = convertToDto(ataJson);
            return convertToModel(cadastroAtaDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o itemJson para objeto Item! \n" + e.getMessage());
        }
    }

    public Ata convertToModel(CadastroAtaDTO cadastroAtaDTO) {
        Ata ata = new Ata();
        BeanUtils.copyProperties(cadastroAtaDTO, ata);
        return ata;
    }

    public CadastroAtaDTO convertToDto(String ataJson) {
        try {
            return this.mapper.readValue(ataJson, CadastroAtaDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o demandaJson para objeto CadastroAtaDTO! \n" + e.getMessage());
        }
    }


    public static List<AtaResumida> converterAtaParaAtaResumida(List<Ata> atas) {
        List<AtaResumida> atasResumidas = new ArrayList<>();
        atas.forEach(ata -> {
            AtaResumida ataResumida = new AtaResumida();
            BeanUtils.copyProperties(ata, ataResumida);
            ataResumida.setIdAta(ata.getIdAta());
            ataResumida.setQtdPropostas(ata.getPropostasLogAta().size());
//            ataResumida.setDataReuniaoAta(ata.getPautaAta().getDataReuniaoPauta());
//            ataResumida.setHorarioInicioAta(ata.getPautaAta().getHorarioInicioPauta());
//            ataResumida.setAnalistaResponsavel(ata.getPautaAta().getAnalistaResponsavelPauta());
            atasResumidas.add(ataResumida);
        });
        return atasResumidas;
    };
}
