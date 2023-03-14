package br.sc.weg.sid.utils;

import br.sc.weg.sid.DTO.CadastroPdfDemandaDTO;
import br.sc.weg.sid.DTO.CadastroPdfPropostaDTO;
import br.sc.weg.sid.DTO.UpdatePropostaDTO;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.PropostaResumida;
import br.sc.weg.sid.model.entities.TamanhoDemanda;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PropostaUtil {

    private ObjectMapper mapper = new ObjectMapper();

    public static List<PropostaResumida> converterPropostaParaPropostaResumida(List<Proposta> propostas) {
        List<PropostaResumida> propostasResumidas = new ArrayList<>();
        propostas.forEach(proposta -> {
            PropostaResumida propostaResumida = new PropostaResumida();
            BeanUtils.copyProperties(proposta, propostaResumida);
            propostaResumida.setDemandaPropostaTitulo(proposta.getDemandaProposta().getTituloDemanda());
            TamanhoDemanda tamanhoDemanda = proposta.getDemandaProposta().getTamanhoDemanda();
            Integer tempoDeExecucaoDemanda = 0;
            if (tamanhoDemanda == TamanhoDemanda.MUITO_GRANDE) {
                tempoDeExecucaoDemanda = 3000;
            } else if (tamanhoDemanda == TamanhoDemanda.GRANDE) {
                tempoDeExecucaoDemanda = 1500;
            } else if (tamanhoDemanda == TamanhoDemanda.MEDIA) {
                tempoDeExecucaoDemanda = 500;
            } else if (tamanhoDemanda == TamanhoDemanda.PEQUENA) {
                tempoDeExecucaoDemanda = 150;
            } else if (tamanhoDemanda == TamanhoDemanda.MUITO_PEQUENA) {
                tempoDeExecucaoDemanda = 20;
            }
            propostaResumida.setTempoDeExecucaoDemanda(tempoDeExecucaoDemanda);
            propostaResumida.setValorDemanda(proposta.getCustosTotaisDoProjeto());
            propostaResumida.setIdDemanda(proposta.getDemandaProposta().getIdDemanda());
            propostaResumida.setLinkPropostaJira(proposta.getLinkJiraProposta());
            propostasResumidas.add(propostaResumida);
        });
        return propostasResumidas;
    }

    ;

    public CadastroPdfPropostaDTO convertToCadastroPdfPropostaDTO(String json) {
        try {
            return this.mapper.readValue(json, CadastroPdfPropostaDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o demandaJson para objeto CadastroPdfDemandaDTO! \n" + e.getMessage());
        }
    }

    public UpdatePropostaDTO convertToUpdateProspotaDTO(String updatePropostaDTO) {
        try {
            return this.mapper.readValue(this.mapper.writeValueAsString(updatePropostaDTO), UpdatePropostaDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter o demandaJson para objeto CadastroPdfDemandaDTO! \n" + e.getMessage());
        }
    }

}
