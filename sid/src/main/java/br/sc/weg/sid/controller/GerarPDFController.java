package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.service.GerarPDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/PDF")
public class GerarPDFController {
    @Autowired
    private final GerarPDFService gerarPDFService;

    public GerarPDFController(GerarPDFService gerarPDFService) {
        this.gerarPDFService = gerarPDFService;
    }

    @GetMapping("/gerar-pdf/{idDemanda}")
    public void gerarPDF(HttpServletResponse response, @PathVariable("idDemanda") int idDemanda) throws Exception {
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_teste.pdf";
        response.setHeader(headerKey, headerValue);

        this.gerarPDFService.export(response, idDemanda);
    }
}
