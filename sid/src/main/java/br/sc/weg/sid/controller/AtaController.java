package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroAtaDTO;
import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.AtaResumida;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.PropostasLog;
import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.model.service.PautaService;
import br.sc.weg.sid.model.service.PropostaLogService;
import br.sc.weg.sid.model.service.PropostaService;
import br.sc.weg.sid.utils.AtaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/ata")
public class AtaController {
    @Autowired
    private AtaService ataService;

    @Autowired
    private PautaService pautaService;

    @Autowired
    private PropostaLogService propostaLogService;

    @Autowired
    private PropostaService propostaService;

    @Autowired
    private GerarPDFAtaController gerarPDFAtaController;

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
            List<PropostasLog> propostasLogs = new ArrayList<>();
            cadastroAtaDTO.setPautaAta(pautaService.findById(cadastroAtaDTO.getPautaAta().getIdPauta()).get());
            cadastroAtaDTO.getPautaAta().getPropostasPauta().forEach(proposta -> {
                PropostasLog propostasLog = new PropostasLog();
                propostasLog.setDemandaValorPropostaLog(proposta.getCustosTotaisDoProjeto());
                propostasLog.setDemandaTituloPropostaLog(proposta.getDemandaProposta().getTituloDemanda());
                Instant periodoExecucaoDemandaInicioInstant = proposta.getPeriodoExecucaoDemandaInicio().toInstant();
                LocalDate dataInicial = periodoExecucaoDemandaInicioInstant.atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDateTime dataHoraInicial = LocalDateTime.of(dataInicial, LocalTime.MIN);

                Instant periodoExecucaoDemandaFimInstant = proposta.getPeriodoExecucaoDemandaFim().toInstant();
                LocalDate dataFinal = periodoExecucaoDemandaFimInstant.atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDateTime dataHoraFinal = LocalDateTime.of(dataFinal, LocalTime.MIN);
                Duration duracao = Duration.between(dataHoraInicial, dataHoraFinal);
                Long diferencaEmHoras = duracao.toHours();
                propostasLog.setDemandaTempoExecucaoPropostaLog(diferencaEmHoras);
                propostasLog.setPropostaPropostaLog(proposta);
                cadastroAtaDTO.getPropostasLogDTO().forEach(propostaLogDTO -> {
                    System.out.println("PROPOSTA LOG DTO: " + propostaLogDTO);
                    Proposta propostaLogFind = propostaService.findById(propostaLogDTO.getPropostaPropostaLogDTO().getIdProposta()).get();
                    if (propostaLogFind.getIdProposta().equals(proposta.getIdProposta())) {
                        System.out.println("Entrou");
                        propostasLog.setConsideracoesProposta(propostaLogDTO.getConsideracoesPropostaLogDTO());
                        propostasLog.setParecerComissaoPropostaLog(propostaLogDTO.getParecerComissaoPropostaLogDTO());
                        propostasLog.setTipoAta(propostaLogDTO.getTipoAtaPropostaLogDTO());
                        propostasLog.setPdfPropostaLog(proposta.getPdfProposta());
                        System.out.println("PDF PROPOSTA LOG: " + propostasLog.getParecerComissaoPropostaLog());
                        System.out.println("PROPOSTAS LOG: " + propostasLogs.size());
                        propostaLogService.save(propostasLog);
                        propostasLogs.add(propostasLog);
                    }
                });
            });
            System.out.println("PROPOSTAS LOG2: " + propostasLogs.size());
            ata.setPropostasLogAta(propostasLogs);
            Ata ataSalva = ataService.save(ata);
            gerarPDFAtaController.generatePDF(ataSalva.getIdAta());
            return ResponseEntity.status(HttpStatus.CREATED).body(ataSalva);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao cadastrar ata \n Message: " + e.getMessage());
        }
    }

    @GetMapping("/pdf-ata/{idAta}")
    ResponseEntity<Object> listarPropostaPdf(@PathVariable("idAta") Integer idAta) {
        try {
            if (ataService.existsById(idAta)) {
                Ata ata = ataService.findById(idAta).get();
                byte[] pdfBytes = ata.getPdfAta();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.builder("inline").filename("pdf-ata-num-" + ata.getIdAta()+".pdf").build());

                return ResponseEntity.ok().headers(headers).body(pdfBytes);
            } else {
                return ResponseEntity.badRequest().body("ERROR 0007: A proposta inserida não existe! ID PROPOSTA: " + idAta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("ERROR 0005: Erro ao buscar pdf da proposta de id: " + idAta + "!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        try {
            ataService.deleteById(id);
            return ResponseEntity.ok().body("Ata deletada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar ata: " + e.getMessage());
        }
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ataService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar ata: " + e.getMessage());
        }
    }

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
