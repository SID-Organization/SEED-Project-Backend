package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.service.PropostaService;
import br.sc.weg.sid.model.service.TabelaCustoLinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/tabela-custo")
public class TabelaCustoController {

    @Autowired
    TabelaCustoLinhaService tabelaCustoLinhaService;

    @Autowired
    PropostaService propostaService;

    @GetMapping()
    ResponseEntity<Object> listarTabelaCusto() {
        try {
            return ResponseEntity.ok().body(tabelaCustoLinhaService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Não existe tabela de custo");
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> listarTabelaCustoPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(tabelaCustoLinhaService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Não existe tabela de custo com id: " + id);
        }
    }

//    @GetMapping("/proposta/{id}")
//    ResponseEntity<Object> listarTabelaCustoPorProposta(@PathVariable Integer id) {
//        Proposta proposta = propostaService.findById(id).get();
//        try {
//            return ResponseEntity.ok().body(tabelaCustoLinhaService.findByProposta(proposta));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Não existe tabela de custo para a proposta com id: " + id);
//        }
//    }

}
