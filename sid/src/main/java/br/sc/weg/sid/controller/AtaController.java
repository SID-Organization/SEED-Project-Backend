package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.utils.AtaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/sid/api/ata")
public class AtaController {
    @Autowired
    private AtaService ataService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<Object> save(
            @RequestParam("pdfAta") MultipartFile pdfAta,
            @RequestParam("documentoAprovacao") MultipartFile documentoAprovacaoAta,
            @RequestParam("ata") String ataJson
    ) {
        AtaUtil ataUtil = new AtaUtil();
        Ata ata = ataUtil.convertJsonToModel(ataJson);

        try {
            ata.setPdfAta(pdfAta.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o pdf");
        }

        try {
            ata.setDocumentoAprovacaoAta(documentoAprovacaoAta.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o documento de aprovação");
        }

        try {
            Ata ataSalva = ataService.save(ata);
            return ResponseEntity.status(HttpStatus.CREATED).body(ataSalva);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar ata \n Message: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        try {
            ataService.deleteById(id);
            return ResponseEntity.ok().body("Ata deletada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar ata: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.ok(ataService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar atas: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ataService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar ata: " + e.getMessage());
        }
    }
}
