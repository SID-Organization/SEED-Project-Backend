package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroDecisaoPropostaDTO;
import br.sc.weg.sid.model.entities.DecisaoProposta;
import br.sc.weg.sid.model.service.DecisaoPropostaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/sid/api/decisao-proposta")
public class DecisaoPropostaController {

    @Autowired
    private DecisaoPropostaService decisaoPropostaService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CadastroDecisaoPropostaDTO cadastroDecisaoPropostaDTO) {
        DecisaoProposta decisaoProposta = new DecisaoProposta();
        BeanUtils.copyProperties(cadastroDecisaoPropostaDTO, decisaoProposta);
        try {
            DecisaoProposta decisaoPropostaSalva = decisaoPropostaService.save(decisaoProposta);
            return ResponseEntity.status(HttpStatus.CREATED).body(decisaoPropostaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar decisão da proposta: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.ok(decisaoPropostaService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar decisões das propostas: " + e.getMessage());
        }
    }


}
