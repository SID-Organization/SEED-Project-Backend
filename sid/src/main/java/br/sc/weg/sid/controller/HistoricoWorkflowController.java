package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroHistoricoWorkflowDTO;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.model.service.HistoricoWorkflowService;
import br.sc.weg.sid.utils.HistoricoWorkflowUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/historico-workflow")
public class HistoricoWorkflowController {

    @Autowired
    private HistoricoWorkflowService historicoWorkflowService;

    @Autowired
    private DemandaService demandaService;

    @GetMapping("/teste")
    public void teste() {
        historicoWorkflowService.teste();
    }

    //Retorna TODOS os históricos de workflow
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        try {
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findAll();
            List<HistoricoWorkflowResumido> historicoWorkflowResumidos = HistoricoWorkflowUtil.converterHistoricoWorkflowParaHistoricoWorkflowReumido(historicoWorkflows);
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(historicoWorkflowResumidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }


    //Insere um novo histórico de workflow no banco de dados

    @PostMapping()
    public ResponseEntity<Object> cadastroHistoricoWorkflow(
            @RequestBody CadastroHistoricoWorkflowDTO historicoWorkflowDTO
    ) {
        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow();
        BeanUtils.copyProperties(historicoWorkflowDTO, historicoWorkflow);
        if (historicoWorkflow.getTarefaHistoricoWorkflow() == TarefaWorkflow.PREENCHER_DEMANDA) {
            historicoWorkflow.setAcaoFeitaHistorico("Enviar");
            historicoWorkflow.setStatusWorkflow(StatusWorkflow.CONCLUIDO);;
            historicoWorkflow.setVersaoHistorico(0.1);
        } else {
            Demanda demanda = demandaService.findById(historicoWorkflow.getDemandaHistorico().getIdDemanda()).get();
            HistoricoWorkflow  historicoWorkflowAnterior = demanda.getHistoricoWorkflowUltimaVersao();
            atualizaStatusWorkflow(historicoWorkflowAnterior.getIdHistoricoWorkflow(), historicoWorkflowAnterior);
            historicoWorkflow.setVersaoHistorico(historicoWorkflowAnterior.getVersaoHistorico());
            if(historicoWorkflow.equals(historicoWorkflowAnterior)) {
                return ResponseEntity.status(HttpStatus.OK).body("Não houveram alterações!");
            }
            historicoWorkflow.setStatusWorkflow(StatusWorkflow.EM_ANDAMENTO);
        }
        LocalDate localDate = LocalDate.now();
        Date dataRecebimento = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        historicoWorkflow.setRecebimentoHistorico(dataRecebimento);
        //Workflow's com status Preencher demanda não tem prazo de conclusão
        if (historicoWorkflow.getTarefaHistoricoWorkflow() == TarefaWorkflow.PREENCHER_DEMANDA) {
            historicoWorkflow.setConclusaoHistorico(dataRecebimento);
        }else {
            localDate = localDate.plusDays(31);
            Date dataPrazo = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            historicoWorkflow.setPrazoHistorico(dataPrazo);
        }
        HistoricoWorkflow historicoWorkflowSalvo = historicoWorkflowService.save(historicoWorkflow);
        try{
            Demanda demandaHistorico = demandaService.findById(historicoWorkflowSalvo.getDemandaHistorico().getIdDemanda()).get();
            demandaHistorico.setHistoricoWorkflowUltimaVersao(historicoWorkflowSalvo);
            demandaService.save(demandaHistorico);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao setar último histórico de workflow da demanda: " + e.getMessage());
        }
        List<HistoricoWorkflow> historicoWorkflowSalvoList = new ArrayList<>();
        historicoWorkflowSalvoList.add(historicoWorkflowSalvo);
        List<HistoricoWorkflowResumido> historicoWorkflowSalvoResumido = HistoricoWorkflowUtil.converterHistoricoWorkflowParaHistoricoWorkflowReumido(
                historicoWorkflowSalvoList);
        return ResponseEntity.status(HttpStatus.CREATED).body(historicoWorkflowSalvoResumido);
    }

    //Busca um histórico de workflow pelo id de uma demanda
    @GetMapping("/demanda/{id}")
    public ResponseEntity<Object> findByDemandaHistorico(@PathVariable("id") Demanda demandaHistorico) {
        try {
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findByDemandaHistorico(demandaHistorico);
            List<HistoricoWorkflowResumido> historicoWorkflowResumidos = HistoricoWorkflowUtil.converterHistoricoWorkflowParaHistoricoWorkflowReumido(historicoWorkflows);
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow de demanda com id: " + demandaHistorico + " encontrado!");
            }
            LocalDateTime localDateTime = LocalDateTime.now();
            Date verificaData = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                historicoWorkflows.forEach(historicoWorkflow -> {
                if(historicoWorkflow.getConclusaoHistorico() != null){
                    if(historicoWorkflow.getConclusaoHistorico().before(verificaData)){
                        historicoWorkflow.setStatusWorkflow(StatusWorkflow.ATRASADO);
                    }
                }
            });
            return ResponseEntity.status(HttpStatus.FOUND).body(historicoWorkflowResumidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }

    //Busca um histórico de workflow pelo número de cadastro de um responsável

    @GetMapping("/responsavel/{numeroCadastroResponsavel}")
    public ResponseEntity<Object> findByResponsavel(@PathVariable("numeroCadastroResponsavel") Usuario numeroCadastroResponsavel) {
        try {
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findByIdResponsavel(numeroCadastroResponsavel);
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado com o responsável de número de cadastro " + numeroCadastroResponsavel + "!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(historicoWorkflows);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }


    //Busca um histórico de workflow pelo status

    @GetMapping("/status-workflow/{statusWorkflow}")
    public ResponseEntity<Object> findByStatusWorkflow(@PathVariable("statusWorkflow") StatusWorkflow statusWorkflow) {
        try {
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findByStatusWorkflow(statusWorkflow);
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado com o status: " + statusWorkflow);
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(historicoWorkflows);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }

    //Atualiza a versão da demanda de um histórico de workflow

    @PutMapping("/atualiza-versao-workflow/{id}")
    public ResponseEntity<Object> atualizaVersaoWorkflow(@PathVariable Integer idHistoricoWorkflow, @RequestBody HistoricoWorkflow historicoWorkflow) {
        try {
            Optional<HistoricoWorkflow> historicoWorkflowOptional = historicoWorkflowService.findById(idHistoricoWorkflow);
            if (historicoWorkflowOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado com o id: " + idHistoricoWorkflow);
            }
            historicoWorkflow.setVersaoHistorico(historicoWorkflow.getVersaoHistorico() + 0.1);
            return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.save(historicoWorkflow));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar versão do histórico de workflow: " + e.getMessage());
        }
    }

    //Atualiza o status de um histórico de workflow
    @PutMapping("/atualiza-status-workflow/{id}")
    public ResponseEntity<Object> atualizaStatusWorkflow(@PathVariable Integer idHistoricoWorkflow, @RequestBody HistoricoWorkflow historicoWorkflow) {
        try {
            Optional<HistoricoWorkflow> historicoWorkflowOptional = historicoWorkflowService.findById(idHistoricoWorkflow);
            if (historicoWorkflowOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado com o id: " + idHistoricoWorkflow);
            }
            LocalDate localDate = LocalDate.now();
            Date dataConclusao = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            historicoWorkflow.setConclusaoHistorico(dataConclusao);
            historicoWorkflow.setStatusWorkflow(StatusWorkflow.CONCLUIDO);
            historicoWorkflow.setAcaoFeitaHistorico("Aprovar");
            return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.save(historicoWorkflow));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar histórico de workflow: " + e.getMessage());
        }
    }

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