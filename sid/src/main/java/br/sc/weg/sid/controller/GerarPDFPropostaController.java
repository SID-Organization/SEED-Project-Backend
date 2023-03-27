package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.GerarPDFDTO;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.model.service.GerarPDFPropostaService;
import br.sc.weg.sid.model.service.PropostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/proposta-PDF")
public class GerarPDFPropostaController {
    @Autowired
    private final GerarPDFPropostaService gerarPDFService;
    @Autowired
    private final DemandaService demandaService;

    @Autowired
    private final PropostaService propostaService;

    public GerarPDFPropostaController(GerarPDFPropostaService gerarPDFService, DemandaService demandaService, PropostaService propostaService) {
        this.gerarPDFService = gerarPDFService;
        this.demandaService = demandaService;
        this.propostaService = propostaService;
    }

    @PostMapping("/gerar-pdf")
    public ResponseEntity<Object> gerarPDF(@RequestBody GerarPDFDTO gerarPDFDTO) throws Exception {

        try{
            if (demandaService.existsById(gerarPDFDTO.getIdDemanda())) {
                if (propostaService.existsById(gerarPDFDTO.getIdProposta())) {
                    Proposta proposta = propostaService.findById(gerarPDFDTO.getIdProposta()).get();
                    ByteArrayInputStream pdf = new ByteArrayInputStream(gerarPDFService.export(gerarPDFDTO.getIdDemanda(), proposta));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("filename",  "proposta-num" + proposta.getIdProposta() + ".pdf");
                    if (propostaService.existsById(gerarPDFDTO.getIdProposta())) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = pdf.read(buffer)) != -1) {
                            baos.write(buffer, 0, length);
                        }
                        proposta.setPdfProposta(baos.toByteArray());
                        propostaService.save(proposta);
                    }else {
                        return ResponseEntity.badRequest().body("Proposta não encontrada");
                    }
                    System.out.println("DPS PDF");
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(baos.toByteArray());
                }else {
                    return ResponseEntity.badRequest().body("Proposta não encontrada");
                }
            } else {
                return ResponseEntity.badRequest().body("Demanda não encontrada");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao gerar PDF: " + e.getMessage());
        }
    }
}