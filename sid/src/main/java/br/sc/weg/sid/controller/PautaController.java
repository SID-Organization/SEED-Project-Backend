package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroPautaDTO;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.service.PautaService;
import br.sc.weg.sid.utils.PautaUtil;
import br.sc.weg.sid.utils.PropostaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/pauta")
public class PautaController {

    @Autowired
    PautaService pautaService;

    @PostMapping
    public ResponseEntity<Object> cadastroPauta(@RequestBody @Valid CadastroPautaDTO cadastroPautaDTO) {
        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(cadastroPautaDTO, pauta);
        Pauta pautaSalva = pautaService.save(pauta);
        return ResponseEntity.ok("Pauta cadastrada com sucesso! \n" + pautaSalva);
    }

    @GetMapping
    public ResponseEntity<Object> listarPautas() {
        try {
            List<Pauta> pautas = pautaService.findAll();
            List<PautaResumida> pautasResumidas = PautaUtil.converterPautaParaPautaResumida(pautas);
            if (pautasResumidas.isEmpty()) {
                return ResponseEntity.ok().body("Nenhuma pauta cadastrada!");
            }
            return ResponseEntity.ok(pautasResumidas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Erro ao listar pautas!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarPautaPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(pautaService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar pauta: " + e.getMessage());
        }
    }

    @GetMapping("/propostas/{id}")
    public ResponseEntity<Object> listarPropostasPorPauta(@PathVariable Integer id) {
        try {
            List<Proposta> propostasPauta = pautaService.findById(id).get().getPropostasPauta();
            List<PropostaResumida> propostasResumidas = PropostaUtil.converterPropostaParaPropostaResumida(propostasPauta);
            if (propostasResumidas.isEmpty()) {
                return ResponseEntity.ok().body("Nenhuma proposta cadastrada para essa pauta!");
            }
            return ResponseEntity.ok().body(propostasResumidas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Pauta com id informado não existe! " + e.getMessage());
        }
    }

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
