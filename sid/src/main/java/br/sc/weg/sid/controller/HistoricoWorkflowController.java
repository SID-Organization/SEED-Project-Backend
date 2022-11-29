package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.service.HistoricoWorkflowService;
import br.sc.weg.sid.utils.HistoricoWorkflowUtil;
import br.sc.weg.sid.utils.UsuarioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/sid/api/historico-workflow")
public class HistoricoWorkflowController {

    @Autowired
    private HistoricoWorkflowService historicoWorkflowService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        try {
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findAll();
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(historicoWorkflows);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping()
    public ResponseEntity<Object> cadastroHistoricoWorkflow(
            @RequestParam(value = "arquivoDemanda") MultipartFile arquivoDemanda,
            @RequestParam(value = "historico") String historicoWorkflowJson
    ) {
        HistoricoWorkflowUtil historicoWorkflowUtil = new HistoricoWorkflowUtil();

        HistoricoWorkflow historicoWorkflow = historicoWorkflowUtil.convertJsonToModel(historicoWorkflowJson);
        historicoWorkflow.setStatusWorkflow(StatusWorkflow.EM_ANDAMENTO);
        historicoWorkflow.setVersaoHistorico(0.1);
        try {
            historicoWorkflow.setPdfHistorico(arquivoDemanda.getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar arquivos: " + e.getMessage());
        }

        HistoricoWorkflow historicoWorkflowSalvo = historicoWorkflowService.save(historicoWorkflow);
        return ResponseEntity.status(HttpStatus.CREATED).body(historicoWorkflowSalvo);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/demanda/{id}")
    public ResponseEntity<Object> findByIdDemanda(@PathVariable("id") Demanda idDemanda) {
        try {
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findByIdDemanda(idDemanda);
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow de demanda com id: " + idDemanda + " encontrado!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(historicoWorkflows);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/usuario/{numeroCadastroResponsavel}")
    public ResponseEntity<Object> findByIdUsuario(@PathVariable("numeroCadastroResponsavel") Usuario numeroCadastroResponsavel) {
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/recebimento-historico/{recebimentoHistorico}")
    public ResponseEntity<Object> findByIdUsuario(@PathVariable("recebimentoHistorico") String recebimentoHistorico) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dataRecebimentoHistorico = formatter.parse(recebimentoHistorico);
            List<HistoricoWorkflow> historicoWorkflows = historicoWorkflowService.findByRecebimentoHistorico(dataRecebimentoHistorico);
            if (historicoWorkflows.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum histórico de workflow encontrado com a data: " + dataRecebimentoHistorico);
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(historicoWorkflows);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar histórico de workflow: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/status-workflow/{statusWorkflow}")
    public ResponseEntity<Object> findByIdUsuario(@PathVariable("statusWorkflow") StatusWorkflow statusWorkflow) {
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

}
