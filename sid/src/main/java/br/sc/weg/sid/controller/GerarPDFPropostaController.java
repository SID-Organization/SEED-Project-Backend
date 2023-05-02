package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.GerarPDFDTO;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.model.service.GerarPDFPropostaService;
import br.sc.weg.sid.model.service.PropostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@RestController
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

    /**
     * Esta função é um mapeamento de requisição HTTP POST que gera um PDF da proposta.
     *
     * @param idProposta - Parâmetro que representa o id da proposta no qual será gerado o pdf.
     * @return ResponseEntity<?> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o PDF da proposta.
     * @throws Exception - Retorna uma mensagem de erro caso não exista um objeto Demanda com o id informado.
     */
    @GetMapping("/gerar-pdf/{idProposta}")
    public ResponseEntity<Object> gerarPDF(@PathVariable("idProposta") Integer idProposta) {
        try {
            Proposta proposta = propostaService.findById(idProposta).get();
            ByteArrayInputStream pdf = new ByteArrayInputStream(gerarPDFService.export(proposta));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "proposta-num" + proposta.getIdProposta() + ".pdf");
            if (propostaService.existsById(proposta.getIdProposta())) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = pdf.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }
                proposta.setPdfProposta(baos.toByteArray());
                propostaService.save(proposta);
            } else {
                return ResponseEntity.badRequest().body("Proposta não encontrada");
            }
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao gerar PDF: " + e.getMessage());
        }
    }
}