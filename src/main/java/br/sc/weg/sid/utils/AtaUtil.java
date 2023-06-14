package br.sc.weg.sid.utils;

import br.sc.weg.sid.DTO.CadastroAtaDTO;
import br.sc.weg.sid.DTO.CadastroPropostaLogDTO;
import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.AtaResumida;
import br.sc.weg.sid.model.entities.PropostasLog;
import br.sc.weg.sid.model.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class AtaUtil {

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
        List<PropostasLog> propostasLogs = new ArrayList<>();
        for (CadastroPropostaLogDTO cadastroPropostaLogDTO : cadastroAtaDTO.getPropostasLog()) {
            PropostasLog propostaLog = new PropostasLog();
            BeanUtils.copyProperties(cadastroPropostaLogDTO, propostaLog);
            propostasLogs.add(propostaLog);
        }
        ata.setPropostasLog(propostasLogs);
        return ata;
    }

    public CadastroAtaDTO convertToDto(String ataJson) {
        try {
            return this.mapper.readValue(ataJson, CadastroAtaDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o ataJson para objeto CadastroAtaDTO! \n" + e.getMessage());
        }
    }


    public static List<AtaResumida> converterAtaParaAtaResumida(List<Ata> atas) {
        List<AtaResumida> atasResumidas = new ArrayList<>();
        atas.forEach(ata -> {
            AtaResumida ataResumida = new AtaResumida();
            BeanUtils.copyProperties(ata, ataResumida);
            ataResumida.setIdAta(ata.getIdAta());
            ataResumida.setQtdPropostas(ata.getPropostasLog().size());
            ataResumida.setDataReuniaoAta(ata.getDataReuniaoPauta());
            ataResumida.setHorarioInicioAta(ata.getHorarioInicioPauta());
            ataResumida.setHorarioTerminoAta(ata.getHorarioTerminoPauta());
            ataResumida.setAnalistaResponsavel(ata.getAnalistaResponsavelPauta().getNomeUsuario());
            ataResumida.setPropostasLog(ata.getPropostasLog());
            atasResumidas.add(ataResumida);
        });
        return atasResumidas;
    };
}
