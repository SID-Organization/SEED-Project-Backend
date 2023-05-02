package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.model.service.GerarPDFDemandaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/demanda-PDF")
public class GerarPDFDemandaController {
    @Autowired
    GerarPDFDemandaService gerarPDFDemandaService;

    @Autowired
    DemandaService demandaService;

    @GetMapping("/gerar-pdf/{idDemanda}")
    public ResponseEntity<Object> generatePDF(@PathVariable("idDemanda") Integer idDemanda) throws Exception {

        if (demandaService.existsById(idDemanda)){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename",  "demanda-num" + idDemanda + ".pdf");
            Demanda demanda = demandaService.findById(idDemanda).get();
            byte[] pdfAta = gerarPDFDemandaService.export(idDemanda);
            demanda.setPdfDemanda(pdfAta);
            demandaService.save(demanda);
            return ResponseEntity.ok().headers(headers).body(pdfAta);
        }else {
            return ResponseEntity.badRequest().body("ERROR 0006: A ata inserida não existe! ID ATA: " + idDemanda);
        }
    }
}
