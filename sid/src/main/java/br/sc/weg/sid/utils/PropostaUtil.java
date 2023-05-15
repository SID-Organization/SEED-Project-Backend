package br.sc.weg.sid.utils;

import br.sc.weg.sid.DTO.*;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.TamanhoDemanda;
import br.sc.weg.sid.model.service.CentroCustoTabelaCustoService;
import br.sc.weg.sid.model.service.TabelaCustoLinhaService;
import br.sc.weg.sid.model.service.TabelaCustoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

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

    public UpdatePropostaDTO convertJsonToModelUpdate(String updatePropostaForm) {
        try {
            UpdatePropostaDTO updatePropostaDTO = convertToUpdateProspotaDTO(updatePropostaForm);
            return updatePropostaDTO;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o updatePropostaForm para Proposta! \n" + e.getMessage());
        }
    }

//    public Proposta convertToModelUpdate(UpdatePropostaDTO updatePropostaDTO) {
//        Proposta proposta = new Proposta();
//        BeanUtils.copyProperties(updatePropostaDTO, proposta);
//        return proposta;
//    }

    public UpdatePropostaDTO convertToUpdateProspotaDTO(String updatePropostaForm) {
        try {
            return this.mapper.readValue(updatePropostaForm, UpdatePropostaDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void criacaoTabelaCusto(
            TabelaCustoService tabelaCustoService,
            List<TabelaCusto> tabelaCustoList,
            UpdatePropostaDTO updatePropostaDTO,
            Proposta proposta,
            TabelaCustoLinhaService tabelaCustoLinhaService,
            CentroCustoTabelaCustoService centroCustoTabelaCustoService
    ) {
        tabelaCustoService.deleteById(tabelaCustoList.get(0).getIdTabelaCusto());
        tabelaCustoService.deleteById(tabelaCustoList.get(1).getIdTabelaCusto());

        for (TabelaCustoLinha tabelaCustoLinha : updatePropostaDTO.getTabelaCusto().get(0).getTabelaCustoLinha()) {
            tabelaCustoLinha.setTabelaCusto(proposta.getTabelaCusto().get(0));
            tabelaCustoLinhaService.save(tabelaCustoLinha);
        }

        for (TabelaCustoLinha tabelaCustoLinha : updatePropostaDTO.getTabelaCusto().get(1).getTabelaCustoLinha()) {
            tabelaCustoLinha.setTabelaCusto(proposta.getTabelaCusto().get(1));
            tabelaCustoLinhaService.save(tabelaCustoLinha);
        }

        for (CentroCustoTabelaCusto centroCustoTabelaCusto : updatePropostaDTO.getTabelaCusto().get(0).getCentroCustoTabelaCusto()) {
            centroCustoTabelaCusto.setTabelaCusto(proposta.getTabelaCusto().get(0));
            centroCustoTabelaCustoService.save(centroCustoTabelaCusto);
        }

        for (CentroCustoTabelaCusto centroCustoTabelaCusto : updatePropostaDTO.getTabelaCusto().get(1).getCentroCustoTabelaCusto()) {
            centroCustoTabelaCusto.setTabelaCusto(proposta.getTabelaCusto().get(1));
            centroCustoTabelaCustoService.save(centroCustoTabelaCusto);
        }

        proposta.getTabelaCusto().get(0).setPropostaTabelaCusto(proposta);
        proposta.getTabelaCusto().get(1).setPropostaTabelaCusto(proposta);
        proposta.getTabelaCusto().get(0).setIdTabelaCusto(tabelaCustoList.get(0).getIdTabelaCusto());
        proposta.getTabelaCusto().get(1).setIdTabelaCusto(tabelaCustoList.get(1).getIdTabelaCusto());
    }


}
