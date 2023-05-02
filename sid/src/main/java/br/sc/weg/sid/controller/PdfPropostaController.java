package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.service.PdfPropostaService;
import br.sc.weg.sid.model.service.PropostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/pdf-proposta")
public class PdfPropostaController {

    @Autowired
    private PdfPropostaService pdfPropostaService;

    @Autowired
    private PropostaService propostaService;

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna todos os objetos PdfProposta cadastrados no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo todos os objetos PdfProposta cadastrados no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não exista objetos PdfProposta cadastrados no banco de dados.
     */
    @GetMapping("/proposta/{id}")
    public ResponseEntity<Object> findByIdProposta(@PathVariable Integer id) {
        Proposta proposta = propostaService.findById(id).get();
        try {
            return ResponseEntity.status(200).body(pdfPropostaService.findByProposta(proposta));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna a ultima versão do objeto PdfProposta cadastrado no banco de dados.
     * @param id - Parâmetro que representa o id da proposta.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo a ultima versão do objeto PdfProposta cadastrado no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não exista objetos PdfProposta cadastrados no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não exista uma proposta cadastrada no banco de dados com o id informado.
     * @throws Exception - Retorna uma mensagem de erro caso não exista objetos PdfProposta cadastrados no banco de dados com o id da proposta informado.
     */
    @GetMapping("/proposta/last/{id}")
    public ResponseEntity<Object> findLastByIdProposta(@PathVariable Integer id) {
        Proposta proposta = propostaService.findById(id).get();
        try {
            return ResponseEntity.status(200).body(pdfPropostaService.findByProposta(proposta).get(pdfPropostaService.findByProposta(proposta).size() - 1));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
