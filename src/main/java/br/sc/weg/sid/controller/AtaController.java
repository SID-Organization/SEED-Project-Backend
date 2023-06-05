package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroAtaDTO;
import br.sc.weg.sid.DTO.CadastroParecerDGAtaDTO;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.TipoAta;
import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.model.service.PautaService;
import br.sc.weg.sid.model.service.PropostaLogService;
import br.sc.weg.sid.model.service.PropostaService;
import br.sc.weg.sid.utils.AtaUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/ata")
@AllArgsConstructor
public class AtaController {

    private AtaService ataService;

    private PautaService pautaService;

    private GerarPDFAtaController gerarPDFAtaController;

    private PropostaService propostaService;

    private PropostaLogService propostaLogService;

    private AtaDGController ataDGController;


    /**
     * Cria uma nova ata a partir dos dados fornecidos pelo usuário e salva no banco de dados.
     * <p>
     * Primeiramente, o método converte os dados da ata em formato JSON para o objeto "CadastroAtaDTO" e para o modelo "Ata" utilizando a
     * classe utilitária "AtaUtil". Em seguida, o documento de aprovação é convertido para um array de bytes e atribuído à propriedade
     * "documentoAprovacaoAta" do objeto "ata".
     * <p>
     * Em seguida, é realizado um loop nas propostas da pauta da ata e nas propostas da ata (que foram preenchidas pelo usuário),
     * para que as informações das propostas possam ser atualizadas nas informações de log das propostas da ata. São atualizadas informações como o valor
     * total da proposta, o título da demanda associada à proposta e o tempo de execução da demanda associada à proposta.
     * <p>
     * Por fim, é salva a ata no banco de dados utilizando o serviço "ataService". Caso a operação de salvamento seja bem-sucedida,
     * é gerado um arquivo PDF da ata utilizando o controlador "gerarPDFAtaController". O método retorna uma ResponseEntity contendo a ata salva ou uma
     * mensagem de erro, caso a operação de salvamento falhe. Em caso de erro na conversão do documento de aprovação,
     * será lançada uma exceção do tipo RuntimeException.
     *
     * @param documentoAprovacaoAta Arquivo que comprova a aprovação da ata.
     * @param ataJson               Dados da ata em formato JSON.
     * @return ResponseEntity contendo a ata salva ou mensagem de erro em caso de falha.
     * @throws RuntimeException se ocorrer algum erro na conversão do documento de aprovação.
     */
    @PostMapping
    public ResponseEntity<Object> save(
            @RequestParam("documentoAprovacao") MultipartFile documentoAprovacaoAta,
            @RequestParam("ata") String ataJson
    ) throws Exception {
        AtaUtil ataUtil = new AtaUtil();
        CadastroAtaDTO cadastroAtaDTO = ataUtil.convertToDto(ataJson);
        Ata ata = ataUtil.convertJsonToModel(ataJson);
        ata.setDocumentoAprovacaoAta(documentoAprovacaoAta.getBytes());
        Pauta pautaAta = pautaService.findById(cadastroAtaDTO.getPautaAta().getIdPauta()).get();
        ata.setDataReuniaoPauta(pautaAta.getDataReuniaoPauta());
        ata.setHorarioInicioPauta(pautaAta.getHorarioInicioPauta());
        ata.setHorarioTerminoPauta(pautaAta.getHorarioTerminoPauta());
        ata.setAnalistaResponsavelPauta(pautaAta.getAnalistaResponsavelPauta());
        pautaAta.getPropostasPauta().forEach(proposta -> {
            ata.getPropostasLog().forEach(ataPropostaLog -> {
                if (proposta.getIdProposta().equals(ataPropostaLog.getPropostaPropostaLog().getIdProposta())) {
                    ataPropostaLog.setDemandaValorPropostaLog(proposta.getCustosTotaisDoProjeto());
                    ataPropostaLog.setDemandaTituloPropostaLog(proposta.getDemandaProposta().getTituloDemanda());
                    ataPropostaLog.setIdDemanda(proposta.getDemandaProposta().getIdDemanda());
                    Instant periodoExecucaoDemandaInicioInstant = proposta.getPeriodoExecucaoDemandaInicio().toInstant();
                    LocalDate dataInicial = periodoExecucaoDemandaInicioInstant.atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDateTime dataHoraInicial = LocalDateTime.of(dataInicial, LocalTime.MIN);
                    Instant periodoExecucaoDemandaFimInstant = proposta.getPeriodoExecucaoDemandaFim().toInstant();
                    LocalDate dataFinal = periodoExecucaoDemandaFimInstant.atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDateTime dataHoraFinal = LocalDateTime.of(dataFinal, LocalTime.MIN);
                    Duration duracao = Duration.between(dataHoraInicial, dataHoraFinal);
                    Long diferencaEmHoras = duracao.toHours();

                    ataPropostaLog.setDemandaTempoExecucaoPropostaLog(diferencaEmHoras);
                    ataPropostaLog.setPropostaPropostaLog(proposta);
                }
            });
        });
        ata.getPropostasLog().forEach(propostaLog -> {
            Proposta proposta = propostaService.findById(propostaLog.getPropostaPropostaLog().getIdProposta()).get();
            propostaLog.setPdfPropostaLog(proposta.getPdfProposta());
        });
        AtomicBoolean existeNaoPublicada = new AtomicBoolean(false);
        AtomicBoolean existePublicada = new AtomicBoolean(false);
        ata.getPropostasLog().forEach(propostaLog -> {
            if (propostaLog.getTipoAtaPropostaLog().toString().equals("NAO_PUBLICADA")) {
                if (!existeNaoPublicada.get()) {
                    existeNaoPublicada.set(true);
                }
            } else {
                if (!existePublicada.get()) {
                    existePublicada.set(true);
                }
            }
        });
        Ata ataSalva = ataService.save(ata);
        if (existePublicada.get()) {
            ataSalva.setNumeroAtaPublicada(ataSalva.getIdAta() + "/" + cadastroAtaDTO.getNumeroAtaPublicada());
        }
        if (existeNaoPublicada.get()) {
            ataSalva.setNumeroAtaNaoPublicada(ataSalva.getIdAta() + "/" + LocalDate.now().getYear());
        }
        ataService.save(ataSalva);
        pautaAta.setStatusPauta(false);
        pautaService.save(pautaAta);
        gerarPDFAtaController.generatePDF(ataSalva.getIdAta());
        return ResponseEntity.status(HttpStatus.CREATED).body(ataSalva);
    }

    @PutMapping("/atualiza-proposta-log")
    public ResponseEntity<Object> update(@RequestBody List<CadastroParecerDGAtaDTO> cadastroParecerDGAtaDTOList) {
        for (CadastroParecerDGAtaDTO cadastroParecerDGAtaDTO : cadastroParecerDGAtaDTOList) {
            if (propostaLogService.existsById(cadastroParecerDGAtaDTO.getIdPropostaLog())) {
                PropostasLog propostasLog = propostaLogService.findById(cadastroParecerDGAtaDTO.getIdPropostaLog()).get();
                BeanUtils.copyProperties(cadastroParecerDGAtaDTO, propostasLog);
                propostaLogService.save(propostasLog);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proposta não encontrada");
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ataDGController.salvarAtaDgByIdAta(cadastroParecerDGAtaDTOList.get(0).getIdAta()).getBody());
    }


    /**
     * Retorna o arquivo PDF da ata publicada correspondente ao ID fornecido.
     *
     * O método recebe o ID da ata desejada como parâmetro na URL e retorna o PDF correspondente da ata publicada.
     * Se a ata existir, é construído um cabeçalho para o PDF e retornado um ResponseEntity contendo o arquivo PDF.
     * Caso contrário, uma mensagem de erro é retornada. O método pode lançar uma exceção em caso de falha na busca do PDF da ata.
     *
     * @param idAta ID da ata publicada a ser buscada.
     * @return ResponseEntity contendo o arquivo PDF da ata publicada ou uma mensagem de erro em caso de falha.
     * @throws RuntimeException se ocorrer algum erro na busca do PDF da ata.
     */
    @GetMapping("/pdf-ata-publicada/{idAta}")
    ResponseEntity<Object> listarAtaPublicadaPdf(@PathVariable("idAta") Integer idAta) {
        return listarAtaPdf(idAta, TipoAta.PUBLICADA);
    }

    /**
     * Retorna o arquivo PDF da ata não publicada correspondente ao ID fornecido.
     *
     * O método recebe o ID da ata desejada como parâmetro na URL e retorna o PDF correspondente da ata não publicada.
     * Se a ata existir, é construído um cabeçalho para o PDF e retornado um ResponseEntity contendo o arquivo PDF.
     * Caso contrário, uma mensagem de erro é retornada. O método pode lançar uma exceção em caso de falha na busca do PDF da ata.
     *
     * @param idAta ID da ata não publicada a ser buscada.
     * @return ResponseEntity contendo o arquivo PDF da ata não publicada ou uma mensagem de erro em caso de falha.
     * @throws RuntimeException se ocorrer algum erro na busca do PDF da ata.
     */
    @GetMapping("/pdf-ata-nao-publicada/{idAta}")
    ResponseEntity<Object> listarAtaNaoPublicadaPdf(@PathVariable("idAta") Integer idAta) {
        return listarAtaPdf(idAta, TipoAta.NAO_PUBLICADA);
    }

    /**
     * Este método é responsável por retornar o arquivo PDF correspondente a uma ata específica, seja ela publicada
     * ou não publicada, com base no ID fornecido. Ele oferece duas variáveis: uma para retornar o PDF da ata publicada
     * e outra para retornar o PDF da ata não publicada.
     *
     * Ao receber o ID da ata desejada como parâmetro na URL, o método busca a ata correspondente no serviço de atendimento.
     * Em seguida, ele percorre a lista de PDFs associados à ata para encontrar o PDF com o tipo de ata desejado.
     * Caso seja encontrado um PDF correspondente, o método constrói um cabeçalho adequado para o arquivo PDF e retorna
     * um ResponseEntity contendo o arquivo.
     * Se nenhum PDF correspondente for encontrado, uma mensagem de erro é retornada informando que a ata não possui PDF
     * do tipo solicitado.
     * Se a ata com o ID fornecido não existir, é retornada uma mensagem de erro indicando que a ata não existe.
     * Em caso de falha durante o processo de busca do PDF da ata, uma exceção pode ser lançada.
     *
     * @param idAta ID da ata desejada.
     * @param tipoAta Tipo de ata desejado (PUBLICADA ou NAO_PUBLICADA).
     * @return ResponseEntity contendo o arquivo PDF da ata correspondente ou uma mensagem de erro em caso de falha.
     * @throws RuntimeException se ocorrer algum erro na busca do PDF da ata.
     */

    private ResponseEntity<Object> listarAtaPdf(Integer idAta, TipoAta tipoAta) {
        try {
            if (ataService.existsById(idAta)) {
                Ata ata = ataService.findById(idAta).get();
                List<PdfAta> pdfAtaList = ata.getPdfAta();
                PdfAta pdfAta = null;

                for (PdfAta pdf : pdfAtaList) {
                    if (pdf.getTipoAta().equals(tipoAta)) {
                        pdfAta = pdf;
                        break;
                    }
                }

                if (pdfAta != null) {
                    byte[] pdfBytes = pdfAta.getPdfAta();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDisposition(ContentDisposition.builder("inline")
                            .filename("pdf-ata-" + tipoAta.name().toLowerCase() + "-numero" + ata.getIdAta() + ".pdf").build());

                    return ResponseEntity.ok().headers(headers).body(pdfBytes);
                } else {
                    return ResponseEntity.badRequest().body("ERROR 0008: A ata de número " + ata.getIdAta() + " não possui um PDF do tipo " +
                            tipoAta.getDescricao().toLowerCase() + "!");
                }
            } else {
                return ResponseEntity.badRequest().body("ERROR 0007: A ata inserida não existe! ID ATA: " + idAta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("ERROR 0005: Erro ao buscar PDF da ata de ID: " + idAta + "!");
    }

    /**
     * Remove uma ata com base no seu ID.
     *
     * @param id ID da ata a ser removida, passado pela URL.
     * @return ResponseEntity indicando sucesso ou falha na operação.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        try {
            ataService.deleteById(id);
            return ResponseEntity.ok().body("Ata deletada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar ata: " + e.getMessage());
        }
    }

    /**
     * Retorna todas as atas presentes no banco de dados em formato resumido.
     *
     * @return ResponseEntity contendo a lista de atas resumidas ou mensagem de erro em caso de falha.
     */
    @GetMapping
    public ResponseEntity<Object> findAll() {
        try {
            List<Ata> atas = ataService.findAll();
            if (atas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma ata encontrada");
            }
            List<AtaResumida> atasResumidas = AtaUtil.converterAtaParaAtaResumida(atas);
            return ResponseEntity.ok(atasResumidas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar atas: " + e.getMessage());
        }
    }

    /**
     * Busca uma ata pelo seu ID.
     *
     * @param id ID da ata a ser buscada, passado pela URL.
     * @return ResponseEntity contendo a ata encontrada ou mensagem de erro em caso de falha.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ataService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar ata: " + e.getMessage());
        }
    }

//    @GetMapping("/pauta-ata/{idPauta}")
//    public ResponseEntity<Object> findByPautaAta(@PathVariable Integer idPauta) {
//        try {
//            Pauta pautaAta = pautaService.findById(idPauta).get();
//            List<Ata> atas = ataService.findByPautaAta(pautaAta);
//            if (atas.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ata com id da pauta: " + idPauta + " não encontrada");
//            }
//            return ResponseEntity.ok(atas);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Erro ao buscar ata: " + e.getMessage());
//        }

}
