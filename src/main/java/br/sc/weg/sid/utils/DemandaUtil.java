package br.sc.weg.sid.utils;

import aj.org.objectweb.asm.TypeReference;
import br.sc.weg.sid.DTO.BuscaDemandaSimilarDTO;
import br.sc.weg.sid.DTO.CadastroDemandaDTO;
import br.sc.weg.sid.DTO.CadastroDemandaSimilarDTO;
import br.sc.weg.sid.DTO.CadastroPdfDemandaDTO;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.*;
import br.sc.weg.sid.model.service.API.client.CotacaoGET;
import br.sc.weg.sid.model.service.DemandaService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Integer retornaQuantidadeStatus(StatusDemanda statusDemanda, DemandaService demandaService) {
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
}
