package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.model.service.PdfDemandaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/pdf-demanda")
public class PdfDemandaController {

    @Autowired
    private PdfDemandaService pdfDemandaService;

    @Autowired
    private DemandaService demandaService;

    @GetMapping("/demanda/{id}")
    public ResponseEntity<?> findByIdDemanda(@PathVariable Integer id) {
        Demanda demanda = demandaService.findById(id).get();
        try {
            return ResponseEntity.status(200).body(pdfDemandaService.findByDemanda(demanda));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(200).body(pdfDemandaService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
