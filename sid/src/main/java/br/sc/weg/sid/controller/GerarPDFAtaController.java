package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.model.service.GerarPDFAtaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/ata-PDF")
public class GerarPDFAtaController {
    @Autowired
    GerarPDFAtaService gerarPDFAtaService;

    @Autowired
    AtaService ataService;

    @GetMapping("/gerar-pdf/{idAta}")
    public ResponseEntity<Object> generatePDF(@PathVariable("idAta") Integer idAta) throws Exception {

        if (ataService.existsById(idAta)){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename",  "ata-num" + idAta + ".pdf");
            Ata ata = ataService.findById(idAta).get();
            byte[] pdfAta = gerarPDFAtaService.export(idAta);
            ata.setPdfAta(pdfAta);
            ataService.save(ata);
            return ResponseEntity.ok().headers(headers).body(pdfAta);
        }else {
            return ResponseEntity.badRequest().body("ERROR 0006: A ata inserida não existe! ID ATA: " + idAta);
        }
    }
}
