package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroHistoricoWorkflowDTO;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.StatusWorkflow;
import br.sc.weg.sid.model.enums.TarefaWorkflow;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.model.service.HistoricoWorkflowService;
import br.sc.weg.sid.model.service.PropostaService;
import br.sc.weg.sid.utils.HistoricoWorkflowUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/historico-workflow")
public class HistoricoWorkflowController {

    @Autowired
    private HistoricoWorkflowService historicoWorkflowService;

    @Autowired
    private DemandaService demandaService;

    @Autowired
    private PropostaService propostaService;

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna todos os históricos de workflow cadastrados no banco de dados.
     *
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo todos os históricos de workflow cadastrados no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não exista históricos de workflow cadastrados no banco de dados.
     */
    //Retorna TODOS os históricos de workflow
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        try {
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findAll();
            List<HistoricoWorkflowResumido> historicoWorkflowResumidos = HistoricoWorkflowUtil.converterHistoricoWorkflowParaHistoricoWorkflowReumido(historicoWorkflows);
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado!");
            }
            return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowResumidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }


    /**
     * Esta função é um mapeamento de requisição HTTP POST que insere um novo histórico de workflow no banco de dados.
     *
     * @param historicoWorkflowDTO - Objeto DTO que contém os dados do histórico de workflow que será inserido no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 201 e o corpo contendo o histórico de workflow cadastrado no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível inserir o histórico de workflow no banco de dados.
     */
    //Insere um novo histórico de workflow no banco de dados
    @PostMapping()
    public ResponseEntity<Object> cadastroHistoricoWorkflow(
            @RequestBody CadastroHistoricoWorkflowDTO historicoWorkflowDTO
    ) {
        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow();
        BeanUtils.copyProperties(historicoWorkflowDTO, historicoWorkflow);
        Demanda demanda = demandaService.findById(historicoWorkflow.getDemandaHistorico().getIdDemanda()).get();
        HistoricoWorkflow historicoWorkflowAnterior = demanda.getHistoricoWorkflowUltimaVersao();
        if (historicoWorkflow.getTarefaHistoricoWorkflow() == TarefaWorkflow.PREENCHER_DEMANDA) {
            historicoWorkflow.setAcaoFeitaHistorico("Enviar");
            historicoWorkflow.setStatusWorkflow(StatusWorkflow.CONCLUIDO);
            historicoWorkflow.setVersaoHistorico(BigDecimal.valueOf(0.1));
        } else if (historicoWorkflow.getTarefaHistoricoWorkflow() == TarefaWorkflow.PROPOSTA_CONCLUIDA) {
            atualizaStatusWorkflow(historicoWorkflowAnterior.getIdHistoricoWorkflow(), historicoWorkflowAnterior);
            historicoWorkflow.setAcaoFeitaHistorico("Enviar");
            historicoWorkflow.setStatusWorkflow(StatusWorkflow.CONCLUIDO);
            historicoWorkflow.setVersaoHistorico(historicoWorkflowAnterior.getVersaoHistorico());
        } else {
            atualizaStatusWorkflow(historicoWorkflowAnterior.getIdHistoricoWorkflow(), historicoWorkflowAnterior);
            if (historicoWorkflowAnterior.getTarefaHistoricoWorkflow() != TarefaWorkflow.PREENCHER_DEMANDA) {
                historicoWorkflowAnterior.setAcaoFeitaHistorico(historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior());
                historicoWorkflowService.save(historicoWorkflowAnterior);
            }
            historicoWorkflow.setVersaoHistorico(historicoWorkflowAnterior.getVersaoHistorico());
            setPdf(historicoWorkflow, demanda);
            if (historicoWorkflow.equals(historicoWorkflowAnterior)) {
                return ResponseEntity.status(HttpStatus.OK).body("Não houveram alterações!");
            }
            historicoWorkflow.setStatusWorkflow(StatusWorkflow.EM_ANDAMENTO);
        }
        LocalDateTime localDate = LocalDateTime.now();
        Date dataRecebimento = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());

        historicoWorkflow.setRecebimentoHistorico(dataRecebimento);
        //Workflow's com status Preencher demanda não tem prazo de conclusão
        if (historicoWorkflow.getTarefaHistoricoWorkflow() == TarefaWorkflow.PREENCHER_DEMANDA ||
                historicoWorkflow.getTarefaHistoricoWorkflow() == TarefaWorkflow.PROPOSTA_CONCLUIDA) {
            Date dataConclusao = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
            historicoWorkflow.setConclusaoHistorico(dataConclusao);
        } else {
            localDate = localDate.plusDays(31);
            Date dataPrazo = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
            historicoWorkflow.setPrazoHistorico(dataPrazo);
        }
        HistoricoWorkflow historicoWorkflowSalvo = historicoWorkflowService.save(historicoWorkflow);
        try {
            Demanda demandaHistorico = demandaService.findById(historicoWorkflowSalvo.getDemandaHistorico().getIdDemanda()).get();
            demandaHistorico.setHistoricoWorkflowUltimaVersao(historicoWorkflowSalvo);
            demandaService.save(demandaHistorico);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao setar último histórico de workflow da demanda: " + e.getMessage());
        }
        List<HistoricoWorkflow> historicoWorkflowSalvoList = new ArrayList<>();
        historicoWorkflowSalvoList.add(historicoWorkflowSalvo);
        List<HistoricoWorkflowResumido> historicoWorkflowSalvoResumido = HistoricoWorkflowUtil.converterHistoricoWorkflowParaHistoricoWorkflowReumido(
                historicoWorkflowSalvoList);
        return ResponseEntity.status(HttpStatus.CREATED).body(historicoWorkflowSalvoResumido);
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna um histórico de workflow cadastrado no banco de dados de acordo com o id da demanda.
     *
     * @param demandaHistorico - Objeto do tipo Demanda que contém o id da demanda que será utilizado para buscar o histórico de workflow no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o histórico de workflow cadastrado no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível encontrar o histórico de workflow no banco de dados.
     */
    //Busca um histórico de workflow pelo id de uma demanda
    @GetMapping("/demanda/{id}")
    public ResponseEntity<Object> findByDemandaHistorico(@PathVariable("id") Demanda demandaHistorico) {
        try {
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findByDemandaHistorico(demandaHistorico);
            List<HistoricoWorkflowResumido> historicoWorkflowResumidos = HistoricoWorkflowUtil.converterHistoricoWorkflowParaHistoricoWorkflowReumido(historicoWorkflows);
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow de demanda com id: " + demandaHistorico.getIdDemanda() + " encontrado!");
            }
            LocalDateTime localDateTime = LocalDateTime.now();
            Date verificaData = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            historicoWorkflows.forEach(historicoWorkflow -> {
                if (historicoWorkflow.getConclusaoHistorico() != null) {
                    if (historicoWorkflow.getConclusaoHistorico().before(verificaData)) {
                        historicoWorkflow.setStatusWorkflow(StatusWorkflow.ATRASADO);
                    }
                }
            });
            return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowResumidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna um histórico de workflow cadastrado no banco de dados de acordo com o número de cadastro de um responsável.
     *
     * @param numeroCadastroResponsavel - Objeto do tipo Usuario que contém o número de cadastro do responsável que será utilizado para buscar o histórico de workflow no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o histórico de workflow cadastrado no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível encontrar o histórico de workflow no banco de dados.
     */
    //Busca um histórico de workflow pelo número de cadastro de um responsável
    @GetMapping("/responsavel/{numeroCadastroResponsavel}")
    public ResponseEntity<Object> findByResponsavel(@PathVariable("numeroCadastroResponsavel") Usuario numeroCadastroResponsavel) {
        try {
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findByIdResponsavel(numeroCadastroResponsavel);
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado com o responsável de número de cadastro " + numeroCadastroResponsavel + "!");
            }
            return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflows);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }

    @GetMapping("/pdf-historico/{tipo}/{idHistoricoWorkflow}")
    public ResponseEntity<Object> buscarPdfHistorico(@PathVariable("tipo") String tipo, @PathVariable("idHistoricoWorkflow") Integer idHistoricoWorkflow) {
        try {
            if (historicoWorkflowService.existsById(idHistoricoWorkflow)) {
                HistoricoWorkflow historicoWorkflow = historicoWorkflowService.findById(idHistoricoWorkflow).get();
                byte[] pdfBytes;

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.builder("inline").filename("historico-" +
                        historicoWorkflow.getTarefaHistoricoWorkflow().getNome().toLowerCase() + ".pdf").build());

                if (tipo.equals("demanda")) {
                    pdfBytes = historicoWorkflow.getPdfHistoricoWorkflowDemanda();
                } else if (tipo.equals("proposta")) {
                    pdfBytes = historicoWorkflow.getPdfHistoricoWorkflowProposta();
                } else {
                    return ResponseEntity.badRequest().body("ERROR: Tipo de PDF inválido!");
                }

                return ResponseEntity.ok().headers(headers).body(pdfBytes);
            } else {
                return ResponseEntity.badRequest().body("ERROR 0007: O histórico inserido não existe! ID HISTÓRICO: " + idHistoricoWorkflow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("ERROR 0005: Erro ao buscar pdf do histórico de id: " + idHistoricoWorkflow + "!");
    }


    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna um histórico de workflow cadastrado no banco de dados de acordo com o status.
     *
     * @param statusWorkflow - Objeto do tipo StatusWorkflow que contém o status que será utilizado para buscar o histórico de workflow no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o histórico de workflow cadastrado no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível encontrar o histórico de workflow no banco de dados.
     */
    //Busca um histórico de workflow pelo status
    @GetMapping("/status-workflow/{statusWorkflow}")
    public ResponseEntity<Object> findByStatusWorkflow(@PathVariable("statusWorkflow") StatusWorkflow statusWorkflow) {
        try {
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findByStatusWorkflow(statusWorkflow);
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado com o status: " + statusWorkflow);
            }
            return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflows);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP PUT que atualiza a versão da demanda de um histórico de workflow.
     *
     * @param idHistoricoWorkflow - Variável do tipo Integer que contém o id do histórico de workflow que será utilizado para atualizar a versão da demanda.
     * @param historicoWorkflow   - Objeto do tipo HistoricoWorkflow que contém a nova versão da demanda que será atualizada no histórico de workflow.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o histórico de workflow com a versão da demanda atualizada.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível atualizar a versão da demanda do histórico de workflow.
     */
    //Atualiza a versão da demanda de um histórico de workflow
    @PutMapping("/atualiza-versao-workflow/{id}")
    public ResponseEntity<Object> atualizaVersaoWorkflow(@PathVariable Integer idHistoricoWorkflow, @RequestBody HistoricoWorkflow historicoWorkflow) {
        try {
            Optional<HistoricoWorkflow> historicoWorkflowOptional = historicoWorkflowService.findById(idHistoricoWorkflow);
            if (historicoWorkflowOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado com o id: " + idHistoricoWorkflow);
            }
            historicoWorkflow.setVersaoHistorico(historicoWorkflow.getVersaoHistorico().add(BigDecimal.valueOf(0.1)));
            Demanda demanda = demandaService.findById(historicoWorkflow.getDemandaHistorico().getIdDemanda()).get();
            setPdf(historicoWorkflow, demanda);
            return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.save(historicoWorkflow));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar versão do histórico de workflow: " + e.getMessage());
        }
    }

    private void setPdf(HistoricoWorkflow historicoWorkflow, Demanda demanda) {
        historicoWorkflow.setPdfHistoricoWorkflowDemanda(demanda.getPdfDemanda());
        List<Proposta> propostasList = propostaService.findByDemandaProposta(demanda);
        if (!propostasList.isEmpty()) {
            Proposta proposta = propostasList.get(propostasList.size() - 1);
            if (proposta.getPdfProposta() != null) {
                historicoWorkflow.setPdfHistoricoWorkflowProposta(proposta.getPdfProposta());
            }
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP PUT que atualiza o status de um histórico de workflow.
     *
     * @param idHistoricoWorkflow - Variável do tipo Integer que contém o id do histórico de workflow que será utilizado para atualizar o status.
     * @param historicoWorkflow   - Objeto do tipo HistoricoWorkflow que contém o novo status que será atualizado no histórico de workflow.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o histórico de workflow com o status atualizado.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível atualizar o status do histórico de workflow.
     */
    //Atualiza o status de um histórico de workflow
    @PutMapping("/atualiza-status-workflow/{id}")
    public ResponseEntity<Object> atualizaStatusWorkflow(@PathVariable Integer idHistoricoWorkflow, @RequestBody HistoricoWorkflow historicoWorkflow) {
        try {
            Optional<HistoricoWorkflow> historicoWorkflowOptional = historicoWorkflowService.findById(idHistoricoWorkflow);
            if (historicoWorkflowOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado com o id: " + idHistoricoWorkflow);
            }
            LocalDateTime localDate = LocalDateTime.now();
            Date dataConclusao = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
            historicoWorkflow.setConclusaoHistorico(dataConclusao);
            historicoWorkflow.setStatusWorkflow(StatusWorkflow.CONCLUIDO);
            return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.save(historicoWorkflow));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar histórico de workflow: " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP DELETE que deleta um histórico de workflow cadastrado no banco de dados de acordo com o id.
     *
     * @param idHistoricoWorkflow - Variável do tipo Integer que contém o id do histórico de workflow que será utilizado para deletar o histórico de workflow no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma mensagem de sucesso ao deletar o histórico de workflow.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível deletar o histórico de workflow no banco de dados.
     */
    //Deleta um histórico de workflow pelo id
    @DeleteMapping("/{idHistoricoWorkflow}")
    public ResponseEntity<Object> deleteById(@PathVariable Integer idHistoricoWorkflow) {
        try {
            Optional<HistoricoWorkflow> historicoWorkflowOptional = historicoWorkflowService.findById(idHistoricoWorkflow);
            if (historicoWorkflowOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado com o id: " + idHistoricoWorkflow);
            }
            historicoWorkflowService.deleteById(idHistoricoWorkflow);
            return ResponseEntity.status(HttpStatus.OK).body("Histórico de workflow com id: " + idHistoricoWorkflow + " deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar histórico de workflow: " + e.getMessage());
        }
    }
}