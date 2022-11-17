package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroPautaDTO;
import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.service.PautaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
