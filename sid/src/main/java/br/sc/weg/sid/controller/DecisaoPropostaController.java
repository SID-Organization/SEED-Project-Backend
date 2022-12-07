package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroDecisaoPropostaDTO;
import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.DecisaoProposta;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.model.service.DecisaoPropostaService;
import br.sc.weg.sid.model.service.PautaService;
import br.sc.weg.sid.model.service.PropostaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/decisao-proposta")
public class DecisaoPropostaController {

    @Autowired
    private DecisaoPropostaService decisaoPropostaService;

    @Autowired
    private PropostaService propostaService;

    @Autowired
    private AtaService ataService;

    @Autowired
    private PautaService pautaService;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        try {
            decisaoPropostaService.deleteById(id);
            return ResponseEntity.ok().body("Decisão da proposta excluída com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar decisão da proposta: " + e.getMessage());
        }
    }

    @GetMapping("/pauta/{id}")
    public ResponseEntity<Object> findByIdPauta(@PathVariable Integer id) {
        try {
            Optional<Pauta> pautaOptional = pautaService.findById(id);
            if (pautaOptional.isPresent()) {
                return ResponseEntity.ok(decisaoPropostaService.findByIdPauta(pautaOptional.get()));
            } else {
                return ResponseEntity.badRequest().body("Pauta não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar decisões das propostas: " + e.getMessage());
        }
    }

    @GetMapping("/proposta/{id}")
    public ResponseEntity<Object> findByIdProposta(@PathVariable Integer id) {
        try {
            Optional<Proposta> proposta = propostaService.findById(id);
            if (proposta.isPresent()) {
                return ResponseEntity.ok(decisaoPropostaService.findByIdProposta(proposta.get()));
            } else {
                return ResponseEntity.badRequest().body("Proposta não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar decisões das propostas: " + e.getMessage());
        }
    }

    @GetMapping("/ata/{id}")
    public ResponseEntity<Object> findByIdAta(@PathVariable Integer id) {
        try {
            Optional<Ata> ata = ataService.findById(id);
            if (ata.isPresent()) {
                return ResponseEntity.ok(decisaoPropostaService.findByIdAta(ata.get()));
            } else {
                return ResponseEntity.badRequest().body("Ata não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar decisões das propostas: " + e.getMessage());
        }
    }
}
