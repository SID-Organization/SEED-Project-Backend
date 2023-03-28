package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.model.service.GerarPDFAtaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void generatePDF(HttpServletResponse response, @PathVariable("idAta") Integer idAta) throws Exception {
        response.setContentType("application/pdf");
        if (ataService.existsById(idAta)){
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=pdf_teste.pdf";
            response.setHeader(headerKey, headerValue);

            this.gerarPDFAtaService.export(response, idAta);
        }else {
            response.setContentType("text/html");
            response.getWriter().write("Ata não encontrada");
        }
    }
}
