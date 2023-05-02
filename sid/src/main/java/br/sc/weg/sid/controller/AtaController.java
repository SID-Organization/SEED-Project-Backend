package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroAtaDTO;
import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.AtaResumida;
import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.model.service.PautaService;
import br.sc.weg.sid.utils.AtaUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/ata")
@AllArgsConstructor
public class AtaController {

    private AtaService ataService;

    private PautaService pautaService;

    private GerarPDFAtaController gerarPDFAtaController;


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
    ) {
        AtaUtil ataUtil = new AtaUtil();
        CadastroAtaDTO cadastroAtaDTO = ataUtil.convertToDto(ataJson);
        Ata ata = ataUtil.convertJsonToModel(ataJson);

        try {
            ata.setDocumentoAprovacaoAta(documentoAprovacaoAta.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o documento de aprovação");
        }

        try {
            ata.setPautaAta(pautaService.findById(cadastroAtaDTO.getPautaAta().getIdPauta()).get());
            ata.getPautaAta().getPropostasPauta().forEach(proposta -> {
                ata.getPropostasLog().forEach(ataPropostaLog -> {
                    if (proposta.getIdProposta().equals(ataPropostaLog.getPropostaPropostaLog().getIdProposta())) {
                        ataPropostaLog.setDemandaValorPropostaLog(proposta.getCustosTotaisDoProjeto());
                        ataPropostaLog.setDemandaTituloPropostaLog(proposta.getDemandaProposta().getTituloDemanda());
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
            Ata ataSalva = ataService.save(ata);
            gerarPDFAtaController.generatePDF(ataSalva.getIdAta());
            return ResponseEntity.status(HttpStatus.CREATED).body(ataSalva);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao cadastrar ata \n Message: " + e.getMessage());
        }
    }


    /**
     * Retorna um arquivo PDF contendo a ata com o ID fornecido pelo usuário.
     * <p>
     * Nessa função, o usuário informa o ID da ata desejada e o método busca e retorna o PDF correspondente.
     * Se a ata existe, o método constrói um cabeçalho para o PDF e retorna um ResponseEntity contendo o arquivo PDF.
     * Caso contrário, retorna uma mensagem de erro. O método pode lançar uma exceção em caso de falha na busca do PDF da ata.
     *
     * @param idAta ID da ata a ser buscada, passado pela URL.
     * @return ResponseEntity contendo o arquivo PDF da ata ou mensagem de erro em caso de falha.
     * @throws RuntimeException se ocorrer algum erro na busca do PDF da ata.
     */
    @GetMapping("/pdf-ata/{idAta}")
    ResponseEntity<Object> listarPropostaPdf(@PathVariable("idAta") Integer idAta) {
        try {
            if (ataService.existsById(idAta)) {
                Ata ata = ataService.findById(idAta).get();
                byte[] pdfBytes = ata.getPdfAta();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.builder("inline").filename("pdf-ata-num-" + ata.getIdAta() + ".pdf").build());

                return ResponseEntity.ok().headers(headers).body(pdfBytes);
            } else {
                return ResponseEntity.badRequest().body("ERROR 0007: A proposta inserida não existe! ID PROPOSTA: " + idAta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("ERROR 0005: Erro ao buscar pdf da proposta de id: " + idAta + "!");
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

    /**
     * Busca uma ata pelo número do DG.
     *
     * @param numeroDgAta Número do DG da ata a ser buscada, passado pela URL.
     * @return ResponseEntity contendo a ata encontrada ou mensagem de erro em caso de falha.
     */
    @GetMapping("/numeroDgAta/{numeroDgAta}")
    public ResponseEntity<Object> findByNumeroDgAta(@PathVariable Integer numeroDgAta) {
        try {
            Ata ataNumeroDG = ataService.findByNumeroDgAta(numeroDgAta);
            if (ataNumeroDG == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ata com numero de DG: " + numeroDgAta + " não encontrada");
            }
            return ResponseEntity.ok(ataNumeroDG);
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
