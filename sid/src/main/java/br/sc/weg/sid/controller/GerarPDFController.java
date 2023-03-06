package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.GerarPDFDTO;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.model.service.GerarPDFService;
import br.sc.weg.sid.model.service.PropostaService;
import com.lowagie.text.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/PDF")
public class GerarPDFController {
    @Autowired
    private final GerarPDFService gerarPDFService;
    @Autowired
    private final DemandaService demandaService;

    @Autowired
    private final PropostaService propostaService;

    public GerarPDFController(GerarPDFService gerarPDFService, DemandaService demandaService, PropostaService propostaService) {
        this.gerarPDFService = gerarPDFService;
        this.demandaService = demandaService;
        this.propostaService = propostaService;
    }

    @PostMapping("/gerar-pdf")
    public ResponseEntity<Object> gerarPDF(HttpServletResponse response, @RequestBody GerarPDFDTO gerarPDFDTO) throws Exception {

        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_teste.pdf";
        response.setHeader(headerKey, headerValue);

        try{
            if (demandaService.existsById(gerarPDFDTO.getIdDemanda())) {
                byte[] pdf = gerarPDFService.export(response, gerarPDFDTO.getIdDemanda()).getBody();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.builder("attachment").filename("pdf_teste.pdf").build());
//                if (propostaService.existsById(gerarPDFDTO.getIdProposta())) {
//                    Proposta proposta = propostaService.findById(gerarPDFDTO.getIdProposta()).get();
//                    proposta.setPropostaPDF(pdf);
//                    propostaService.save(proposta);
//                }else {
//                    return ResponseEntity.badRequest().body("Proposta não encontrada");
//                }
                return ResponseEntity.ok().headers(headers).body(pdf);
            } else {
                return ResponseEntity.badRequest().body("Demanda não encontrada");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao gerar PDF: " + e.getMessage());
        }
    }

}
