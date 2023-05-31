package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.MotivoRecusa;
import br.sc.weg.sid.model.enums.StatusDemanda;
import br.sc.weg.sid.model.service.MotivoRecusaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/motivo-recusa")
@AllArgsConstructor
public class MotivoRecusaController {

    MotivoRecusaService motivoRecusaService;


    @GetMapping("/devolucao/{idWorkflow}")
    public ResponseEntity<Object> buscarMotivoRecusaDevolucaoPorIdWorkflow(@PathVariable Integer idWorkflow) {
        try {
            MotivoRecusa motivoRecusa = motivoRecusaService.findAllByIdHistoricoWorkflowAndStatusDemandaMotivoRecusa(idWorkflow, StatusDemanda.EM_EDICAO);
            if (motivoRecusa == null) {
                return ResponseEntity.ok("Motivo de recusa não encontrado!");
            }
            return ResponseEntity.ok(motivoRecusa.getDescricaoMotivoRecusa());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao buscar motivo de recusa de devolução: " + e.getMessage());
        }
    }

}
