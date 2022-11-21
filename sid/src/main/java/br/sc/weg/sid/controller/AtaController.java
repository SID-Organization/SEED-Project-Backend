package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.utils.AtaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
}
