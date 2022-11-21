package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroPautaDTO;
import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.service.PautaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/sid/api/pauta")
public class PautaController {

    @Autowired
    PautaService pautaService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<Object> cadastroPauta(@RequestBody @Valid CadastroPautaDTO cadastroPautaDTO) {
        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(cadastroPautaDTO, pauta);
        Pauta pautaSalva = pautaService.save(pauta);
        return ResponseEntity.ok("Pauta cadastrada com sucesso! \n" + pautaSalva);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<Object> listarPautas() {
        try {
            return ResponseEntity.ok(pautaService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar pautas: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<Object> listarPautaPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(pautaService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar pauta: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarPauta(@PathVariable Integer id) {
        try {
            pautaService.deleteById(id);
            return ResponseEntity.ok("Pauta deletada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar pauta: " + e.getMessage());
        }
    }

}
