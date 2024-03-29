package br.sc.weg.sid.utils;

import br.sc.weg.sid.DTO.*;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.*;
import br.sc.weg.sid.model.service.API.client.CotacaoGET;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.model.service.HistoricoStatusDemandaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.NoArgsConstructor;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@NoArgsConstructor
@Service
public class DemandaUtil {

    private ObjectMapper mapper = new ObjectMapper();

    public Demanda convertDtoToModel(CadastroDemandaDTO cadastroDemandaDTO) {
        Demanda demanda = new Demanda();
        BeanUtils.copyProperties(cadastroDemandaDTO, demanda);
        return demanda;
    }

    public CadastroDemandaDTO convertToDto(String demandaJson) {
        try {
            return this.mapper.readValue(demandaJson, CadastroDemandaDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o demandaJson para objeto CadastroDemandaDTO! \n" + e.getMessage());
        }
    }

    public CadastroPdfDemandaDTO convertToPdfDto(String pdfDemandaJson) {
        try {
            System.out.println("PDF: " + pdfDemandaJson);
            return this.mapper.readValue(pdfDemandaJson, CadastroPdfDemandaDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao converter o demandaJson para objeto CadastroPdfDemandaDTO! \n" + e.getMessage());
        }
    }

    public PdfDemanda convertPdfDtoToModel(CadastroPdfDemandaDTO cadastroPdfDemandaDTO) {
        PdfDemanda pdfDemanda = new PdfDemanda();
        BeanUtils.copyProperties(cadastroPdfDemandaDTO, pdfDemanda);
        return pdfDemanda;
    }

    public List<DemandaResumida> resumirDemanda(List<Demanda> demandas) {
        List<DemandaResumida> demandasResumidas = new ArrayList<>();
        for (Demanda demanda : demandas) {
            DemandaResumida demandaResumida = new DemandaResumida();
            demandaResumida.setTituloDemanda(demanda.getTituloDemanda());
            demandaResumida.setStatusDemanda(demanda.getStatusDemanda());
            demandaResumida.setPrazoElaboracaoDemanda(demanda.getPrazoElaboracaoDemanda());
            demandaResumida.setScoreDemanda(demanda.getScoreDemanda());
            demandaResumida.setCustoTotalDemanda(demanda.getCustoTotalDemanda());
            demandaResumida.setIdDemanda(demanda.getIdDemanda());
            demandaResumida.setNomeSolicitante(demanda.getSolicitanteDemanda().getNomeUsuario());
            if (demanda.getAnalistasResponsaveisDemanda() != null) {
                StringBuilder analistas = new StringBuilder();
                for (int i = 0; i < demanda.getAnalistasResponsaveisDemanda().size(); i++) {
                    Usuario analista = demanda.getAnalistasResponsaveisDemanda().get(i);
                    analistas.append(analista.getNomeUsuario());

                    if (i < demanda.getAnalistasResponsaveisDemanda().size() - 1) {
                        analistas.append(", ");
                    }
                }
                demandaResumida.setNomesAnalistasResponsaveis(analistas.toString());
            }
            demandaResumida.setNomeGerenteResponsavelDemanda(demanda.getGerenteDaAreaDemanda().getNomeUsuario());
            if (demanda.getStatusDemanda() == StatusDemanda.CANCELADA) {
                demanda.getMotivosRecusaDemanda().forEach(motivoRecusa -> {
                    if (motivoRecusa.getStatusDemandaMotivoRecusa() == StatusDemanda.CANCELADA) {
                        demandaResumida.setMotivoRecusaDemanda(motivoRecusa.getDescricaoMotivoRecusa());
                    }
                });
            }

            if (demanda.getBuSolicitanteDemanda() != null && demanda.getBuSolicitanteDemanda().getNomeBusinessUnity() != null) {
                demandaResumida.setDepartamentoDemanda(demanda.getBuSolicitanteDemanda().getNomeBusinessUnity());
            } else {
                demandaResumida.setDepartamentoDemanda(null);
            }
            if (demanda.getTamanhoDemanda() != null) {
                demandaResumida.setTamanhoDemanda(demanda.getTamanhoDemanda().getNome());
            } else {
                demandaResumida.setTamanhoDemanda(null);
            }
            demandaResumida.setCodigoPPMDemanda(demanda.getCodigoPPMDemanda());
            demandasResumidas.add(demandaResumida);
        }
        return demandasResumidas;
    }

    public Double retornaBeneficioRealSomado(List<Beneficio> beneficioList) {
        Double valorSomado = 0.0;

        for (Beneficio beneficio : beneficioList) {
            if (beneficio.getTipoBeneficio().equals(TipoBeneficio.REAL)) {
                valorSomado = getaDouble(valorSomado, beneficio);
            }
        }
        return valorSomado;
    }

    public Double retornaBeneficioPotencialSomado(List<Beneficio> beneficioList) {
        Double valorSomado = 0.0;

        for (Beneficio beneficio : beneficioList) {
            if (beneficio.getTipoBeneficio().equals(TipoBeneficio.POTENCIAL)) {
                valorSomado = getaDouble(valorSomado, beneficio);
            }
        }
        return valorSomado;
    }

    @NotNull
    private Double getaDouble(Double valorSomado, Beneficio beneficio) {
        if (beneficio.getMoedaBeneficio().equals(Moeda.DOLAR)) {
            CotacaoGET cotacaoGET = new CotacaoGET();
            double soma = beneficio.getValorBeneficio() / cotacaoGET.getCotacaoDolar();
            valorSomado += soma;
        } else if (beneficio.getMoedaBeneficio().equals(Moeda.EURO)) {
            CotacaoGET cotacaoGET = new CotacaoGET();
            double soma = beneficio.getValorBeneficio() / cotacaoGET.getCotacaoEuro();
            valorSomado += soma;
        } else {
            valorSomado += beneficio.getValorBeneficio();
        }
        return valorSomado;
    }

    public Integer retornaValorClassificacao(Demanda demanda) {
        if (demanda.getTamanhoDemanda().equals(TamanhoDemanda.MUITO_GRANDE)) {
            return 5000;
        } else if (demanda.getTamanhoDemanda().equals(TamanhoDemanda.GRANDE)) {
            return 3000;
        } else if (demanda.getTamanhoDemanda().equals(TamanhoDemanda.MEDIA)) {
            return 1000;
        } else if (demanda.getTamanhoDemanda().equals(TamanhoDemanda.PEQUENA)) {
            return 300;
        } else {
            return 40;
        }
    }

    public Integer retornaValorImportancia(Demanda demanda) {
        if (demanda.getImportanciaDemanda() == null) {
            return 1;
        }

        if (demanda.getImportanciaDemanda().equals(ImportanciaDemanda.TRIVIAL)) {
            return 1;
        } else if (demanda.getImportanciaDemanda().equals(ImportanciaDemanda.MINOR)) {
            return 2;
        } else if (demanda.getImportanciaDemanda().equals(ImportanciaDemanda.MAJOR)) {
            return 4;
        } else if (demanda.getImportanciaDemanda().equals(ImportanciaDemanda.CRITICAL)) {
            return 16;
        } else if (demanda.getImportanciaDemanda().equals(ImportanciaDemanda.BLOCKER)) {
            return 100000;
        } else {
            return 1;
        }
    }

    public long calcularDiasDesdeCriacao(Date dataCriacaoDemanda) {
        if (dataCriacaoDemanda == null) {
            return 1;
        }
        LocalDate dataCriacao = dataCriacaoDemanda.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dataAtual = LocalDate.now();
        long dias = ChronoUnit.DAYS.between(dataCriacao, dataAtual);
        if (dias == 0) {
            return 1;
        }
        return dias;
    }

    public Double retornaScoreDemandaCriacao(Demanda demanda) {
        Double beneficioRealSomado = retornaBeneficioRealSomado(demanda.getBeneficiosDemanda());
        Double beneficioPotencialSomado = retornaBeneficioPotencialSomado(demanda.getBeneficiosDemanda());

        double score = ((((2 * beneficioRealSomado) + (1 * beneficioPotencialSomado) + calcularDiasDesdeCriacao(demanda.getDataCriacaoDemanda()))) / 1000000000) * retornaValorImportancia(demanda);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        String formattedScore = df.format(score);
        String formattedScoreWithDot = formattedScore.replace(",", ".");
        return Double.valueOf(formattedScoreWithDot);
    }

    public Double retornaScoreDemandaClassificacao(Demanda demanda) {
        Double beneficioRealSomado = retornaBeneficioRealSomado(demanda.getBeneficiosDemanda());
        Double beneficioPotencialSomado = retornaBeneficioPotencialSomado(demanda.getBeneficiosDemanda());

        double score = ((((2 * beneficioRealSomado) + (1 * beneficioPotencialSomado) + calcularDiasDesdeCriacao(demanda.getDataCriacaoDemanda()))) / retornaValorClassificacao(demanda)) * retornaValorImportancia(demanda);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        String formattedScore = df.format(score);
        String formattedScoreWithDot = formattedScore.replace(",", ".");
        return Double.valueOf(formattedScoreWithDot);
    }

    public Demanda atualizaScoreDiario(Demanda demanda) {
        if (demanda.getTamanhoDemanda() != null) {
            demanda.setScoreDemanda(retornaScoreDemandaClassificacao(demanda));
            return demanda;
        } else {
            demanda.setScoreDemanda(retornaScoreDemandaCriacao(demanda));
            return demanda;
        }
    }

    public Double retornaScoreDemandaImportancia(Demanda demanda) {
        Double beneficioRealSomado = retornaBeneficioRealSomado(demanda.getBeneficiosDemanda());
        Double beneficioPotencialSomado = retornaBeneficioPotencialSomado(demanda.getBeneficiosDemanda());

        double score;

        if (demanda.getTamanhoDemanda() != null && demanda.getImportanciaDemanda() != ImportanciaDemanda.BLOCKER) {
            score = ((((2 * beneficioRealSomado) + (1 * beneficioPotencialSomado) + calcularDiasDesdeCriacao(demanda.getDataCriacaoDemanda()))) / retornaValorClassificacao(demanda)) * retornaValorImportancia(demanda);
        } else if (demanda.getImportanciaDemanda() != ImportanciaDemanda.BLOCKER) {
            score = ((((2 * beneficioRealSomado) + (1 * beneficioPotencialSomado) + calcularDiasDesdeCriacao(demanda.getDataCriacaoDemanda()))) / 1000000000) * retornaValorImportancia(demanda);
        } else if (demanda.getTamanhoDemanda() != null) {
            score = ((((2 * beneficioRealSomado) + (1 * beneficioPotencialSomado) + calcularDiasDesdeCriacao(demanda.getDataCriacaoDemanda()))) / retornaValorClassificacao(demanda)) + retornaValorImportancia(demanda);
        } else {
            score = ((((2 * beneficioRealSomado) + (1 * beneficioPotencialSomado) + calcularDiasDesdeCriacao(demanda.getDataCriacaoDemanda()))) / 1000000000) + retornaValorImportancia(demanda);
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        String formattedScore = df.format(score);
        String formattedScoreWithDot = formattedScore.replace(",", ".");
        System.out.println("Importância demanda: " + demanda.getImportanciaDemanda());
        return Double.valueOf(formattedScoreWithDot);
    }

    public boolean verificaCamposDemandaSimiliar(Demanda demanda) {
        return demanda.getTituloDemanda() != null && demanda.getPropostaMelhoriaDemanda() != null && demanda.getDescricaoQualitativoDemanda() != null && demanda.getFrequenciaUsoDemanda() != null && demanda.getSituacaoAtualDemanda() != null;
    }


    public boolean cadastraDemandaSimilar(CadastroDemandaSimilarDTO cadastroDemandaSimilarDTO) {
        OkHttpClient client = new OkHttpClient();

        String url = "http://localhost:5000/demandas";

        MediaType mediaType = MediaType.parse("application/json");
        //exemplo json: {
        //    "id_demanda": "24",
        //    "titulo": "Carro recreativo",
        //    "proposta_melhoria": "Criar um carro recreativo para que meu filho possa se divertir",
        //    "descricao_qualitativo": "Meu filho anda reclamando que não tem nada legal para brincar",
        //    "frequencia_uso_demanda": "Muito alta",
        //    "situacao_atual_demanda": "Em progresso"
        //}

        RequestBody body = RequestBody.create(mediaType, "{\"id_demanda\": \"" + cadastroDemandaSimilarDTO.getIdDemanda() + "\",\"titulo\": \"" + cadastroDemandaSimilarDTO.getTituloDemanda() + "\",\"proposta_melhoria\": \"" + cadastroDemandaSimilarDTO.getPropostaMelhoriaDemanda() + "\",\"descricao_qualitativo\": \"" + cadastroDemandaSimilarDTO.getDescricaoQualitativoDemanda() + "\",\"frequencia_uso_demanda\": \"" + cadastroDemandaSimilarDTO.getFrequenciaUsoDemanda() + "\",\"situacao_atual_demanda\": \"" + cadastroDemandaSimilarDTO.getSituacaoAtualDemanda() + "\"}");
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            System.out.println(response.body().string());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BuscaDemandaSimilarDTO> buscarDemandaSimilares(Demanda demanda) {
        OkHttpClient client = new OkHttpClient();

        String url = "http://localhost:5000/demandas/similar/" + demanda.getIdDemanda();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            String json = response.body().string();

            Gson gson = new Gson();
            List<BuscaDemandaSimilarDTO> demandasSimilares = gson.fromJson(json, new TypeToken<List<BuscaDemandaSimilarDTO>>() {
            }.getType());

            System.out.println(json);
            return demandasSimilares;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PreverDemandaDTO> preverDemanda() {
        OkHttpClient client = new OkHttpClient();

        String url = "http://localhost:5000/demandas/prever";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            String json = response.body().string();

            Gson gson = new Gson();
            List<PreverDemandaDTO> previsaoDemanda = gson.fromJson(json, new TypeToken<List<PreverDemandaDTO>>() {
            }.getType());

            System.out.println(json);
            return previsaoDemanda;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer retornaQuantidadeStatusAtual(StatusDemanda statusDemanda, DemandaService demandaService) {
        if (statusDemanda == StatusDemanda.CLASSIFICADO_PELO_ANALISTA) {
            return demandaService.findDemandasClassificadasPeloAnalista().size();
        }
        if (statusDemanda == StatusDemanda.ABERTA) {
            return demandaService.findDemandasAbertas().size();
        }
        if (statusDemanda == StatusDemanda.EM_EDICAO) {
            return demandaService.findDemandasEmEdicao().size();
        }
        if (statusDemanda == StatusDemanda.APROVADO_PELO_GERENTE_DA_AREA) {
            return demandaService.findDemandasAprovadasPeloGerenteDaArea().size();
        }
        if (statusDemanda == StatusDemanda.PROPOSTA_EM_ELABORACAO) {
            return demandaService.findDemandasPropostaEmElaboracao().size();
        }
        if (statusDemanda == StatusDemanda.PROPOSTA_PRONTA) {
            return demandaService.findDemandasPropostaPronta().size();
        }
        if (statusDemanda == StatusDemanda.EM_PAUTA) {
            return demandaService.findDemandasEmPauta().size();
        }
        if (statusDemanda == StatusDemanda.APROVADA_EM_COMISSAO) {
            return demandaService.findDemandasAprovadasEmComissao().size();
        }
        if (statusDemanda == StatusDemanda.APROVADA_EM_DG) {
            return demandaService.findDemandasAprovadasEmDG().size();
        }
        if (statusDemanda == StatusDemanda.PROPOSTA_EM_EXECUCAO) {
            return demandaService.findDemandasPropostaEmExecucao().size();
        }
        if (statusDemanda == StatusDemanda.BUSINESS_CASE) {
            return demandaService.findDemandasBusinessCase().size();
        }
        if (statusDemanda == StatusDemanda.PROPOSTA_EM_SUPORTE) {
            return demandaService.findDemandasPropostaEmSuporte().size();
        }
        if (statusDemanda == StatusDemanda.PROPOSTA_FINALIZADA) {
            return demandaService.findDemandasPropostaFinalizada().size();
        }
        if (statusDemanda == StatusDemanda.CANCELADA) {
            return demandaService.findDemandasCanceladas().size();
        }
        if (statusDemanda == StatusDemanda.RASCUNHO) {
            return demandaService.findDemandasRascunho().size();
        }

        return -1;
    }

    public List<RetornoHistoricoStatusDemandaDTO> retornaQuantidadeStatusDemanda(HistoricoStatusDemandaService historicoStatusDemandaService) {
        List<HistoricoStatusDemanda> historicoStatusDemandas = historicoStatusDemandaService.findAll();
        List<RetornoHistoricoStatusDemandaDTO> retornoHistoricoStatusDemandaDTOS = new ArrayList<>();

        // Mapear os status para seus respectivos DTOs
        Map<StatusDemanda, RetornoHistoricoStatusDemandaDTO> statusMap = new HashMap<>();
        statusMap.put(StatusDemanda.ABERTA, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.EM_EDICAO, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.APROVADO_PELO_GERENTE_DA_AREA, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.PROPOSTA_EM_ELABORACAO, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.PROPOSTA_PRONTA, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.EM_PAUTA, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.APROVADA_EM_COMISSAO, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.APROVADA_EM_DG, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.PROPOSTA_EM_EXECUCAO, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.BUSINESS_CASE, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.PROPOSTA_EM_SUPORTE, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.PROPOSTA_FINALIZADA, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.CANCELADA, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.RASCUNHO, new RetornoHistoricoStatusDemandaDTO());
        statusMap.put(StatusDemanda.CLASSIFICADO_PELO_ANALISTA, new RetornoHistoricoStatusDemandaDTO());

        // Percorrer os registros de HistoricoStatusDemanda e contabilizar as quantidades
        for (HistoricoStatusDemanda historicoStatusDemanda : historicoStatusDemandas) {
            RetornoHistoricoStatusDemandaDTO retornoHistoricoStatusDemandaDTO = statusMap.get(historicoStatusDemanda.getStatusDemanda());
            if (retornoHistoricoStatusDemandaDTO != null) {
                if (retornoHistoricoStatusDemandaDTO.getDados() == null) {
                    retornoHistoricoStatusDemandaDTO.setDados(new ArrayList<>());
                }
                String monthYear = getMonthYear(historicoStatusDemanda.getDataAlteracaoStatusDemanda());
                RetornoHistoricoStatusDemandaPayloadDTO payloadDTO = findPayloadByData(retornoHistoricoStatusDemandaDTO.getDados(), monthYear);
                if (payloadDTO == null) {
                    payloadDTO = new RetornoHistoricoStatusDemandaPayloadDTO();
                    payloadDTO.setData(monthYear);
                    payloadDTO.setQuantidade(1);
                    retornoHistoricoStatusDemandaDTO.getDados().add(payloadDTO);
                } else {
                    payloadDTO.setQuantidade(payloadDTO.getQuantidade() + 1);
                }
            }
        }

        // Gerar a lista final de RetornoHistoricoStatusDemandaDTO
        for (StatusDemanda status : statusMap.keySet()) {
            RetornoHistoricoStatusDemandaDTO dto = statusMap.get(status);
            if (dto.getDados() != null && !dto.getDados().isEmpty()) {
                dto.setStatus(status);
                retornoHistoricoStatusDemandaDTOS.add(dto);
            }
        }

        return retornoHistoricoStatusDemandaDTOS;
    }

    // Função auxiliar para obter o mês e o ano a partir de uma data
    private String getMonthYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return String.format("%02d/%04d", month, year);
    }

    // Função auxiliar para encontrar o RetornoHistoricoStatusDemandaPayloadDTO pelo mês e ano
    private RetornoHistoricoStatusDemandaPayloadDTO findPayloadByData(List<RetornoHistoricoStatusDemandaPayloadDTO> payloads, String monthYear) {
        for (RetornoHistoricoStatusDemandaPayloadDTO payload : payloads) {
            if (payload.getData().equals(monthYear)) {
                return payload;
            }
        }
        return null;
    }
}
