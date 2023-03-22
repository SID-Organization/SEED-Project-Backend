package br.sc.weg.sid.utils;

import br.sc.weg.sid.DTO.*;
import br.sc.weg.sid.model.entities.*;
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
            propostaResumida.setIdProposta(proposta.getIdProposta());
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


    public PdfProposta convertJsonToModel(String pdfPropostaJson) {
        try {
            CadastroPdfPropostaDTO cadastroPdfPropostaDTO = convertToCadastroPdfPropostaDTO(pdfPropostaJson);
            return convertToModel(cadastroPdfPropostaDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o itemJson para objeto Item! \n" + e.getMessage());
        }
    }

    public PdfProposta convertToModel(CadastroPdfPropostaDTO cadastroPdfPropostaDTO) {
        PdfProposta pdfProposta = new PdfProposta();
        BeanUtils.copyProperties(cadastroPdfPropostaDTO, pdfProposta);
        return pdfProposta;
    }

    public CadastroPdfPropostaDTO convertToCadastroPdfPropostaDTO(String pdfPropostaJson) {
        try {
            return this.mapper.readValue(pdfPropostaJson, CadastroPdfPropostaDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o pdfPropostaJson para objeto CadastroPdfPropostaDTO! \n" + e.getMessage());
        }
    }

    public Proposta convertDtoToModel(UpdatePropostaDTO updatePropostaDTO) {
        return this.mapper.convertValue(updatePropostaDTO, Proposta.class);
    }

    public UpdatePropostaDTO convertToUpdateProspotaDTO(String updatePropostaForm) {
        try{
            return this.mapper.readValue(updatePropostaForm, UpdatePropostaDTO.class);
        } catch (Exception e){
            throw  new RuntimeException(e);
        }
    }


}
