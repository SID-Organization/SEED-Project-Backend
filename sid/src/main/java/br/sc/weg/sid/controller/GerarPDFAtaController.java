package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.model.service.GerarPDFAtaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/ata-PDF")
public class GerarPDFAtaController {
    @Autowired
    GerarPDFAtaService gerarPDFAtaService;

    @Autowired
    AtaService ataService;

    @GetMapping("/gerar-pdf/{idAta}")
    public ResponseEntity<Object> generatePDF(HttpServletResponse response, @PathVariable("idAta") Integer idAta) throws Exception {
        response.setContentType("application/pdf");
        if (ataService.existsById(idAta)){
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=pdf_teste.pdf";
            response.setHeader(headerKey, headerValue);
            Ata ata = ataService.findById(idAta).get();
            byte[] pdfAta = gerarPDFAtaService.export(response, idAta);
            ata.setPdfAta(pdfAta);
            return ResponseEntity.ok().body(pdfAta);
        }else {
            return ResponseEntity.badRequest().body("ERROR 0006: A ata inserida não existe! ID ATA: " + idAta);
        }
    }
}
