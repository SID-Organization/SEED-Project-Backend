package br.sc.weg.sid.utils;

import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.PautaResumida;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class PautaUtil {

    private ObjectMapper mapper = new ObjectMapper();

    public static List<PautaResumida> converterPautaParaPautaResumida(List<Pauta> pautas) {
        List<PautaResumida> pautasResumidas = new ArrayList<>();
        pautas.forEach(pauta -> {
            if (!pauta.isStatusPauta()) {
                return;
            }
            PautaResumida pautaResumida = new PautaResumida();
            BeanUtils.copyProperties(pauta, pautaResumida);
            pautaResumida.setIdPauta(pauta.getIdPauta());
            pautaResumida.setQtdPropostas(pauta.getPropostasPauta().size());
            pautaResumida.setDataReuniao(pauta.getDataReuniaoPauta());
            pautaResumida.setHoraReuniao(pauta.getHorarioInicioPauta());
            pautaResumida.setHoraTerminoReuniao(pauta.getHorarioTerminoPauta());
            pautaResumida.setAnalistaResponsavel(pauta.getAnalistaResponsavelPauta().getNomeUsuario());
            pautasResumidas.add(pautaResumida);
        });
        return pautasResumidas;
    };
}
