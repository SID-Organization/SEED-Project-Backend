package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.service.PdfPropostaService;
import br.sc.weg.sid.model.service.PropostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/pdf-proposta")
public class PdfPropostaController {

    @Autowired
    private PdfPropostaService pdfPropostaService;

    @Autowired
    private PropostaService propostaService;

    @GetMapping("/proposta/{id}")
    public ResponseEntity<Object> findByIdProposta(@PathVariable Integer id) {
        Proposta proposta = propostaService.findById(id).get();
        try {
            return ResponseEntity.status(200).body(pdfPropostaService.findByProposta(proposta));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/proposta/last/{id}")
    public ResponseEntity<Object> findLastByIdProposta(@PathVariable Integer id) {
        Proposta proposta = propostaService.findById(id).get();
        try {
            return ResponseEntity.status(200).body(pdfPropostaService.findByProposta(proposta).get(pdfPropostaService.findByProposta(proposta).size() - 1));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
