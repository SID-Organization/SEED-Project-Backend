package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroComissaoDTO;
import br.sc.weg.sid.model.entities.Comissao;
import br.sc.weg.sid.model.service.ComissaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sid/api/comissao")
public class ComissaoController {

    @Autowired
    ComissaoService comissaoService;

    @PostMapping()
    ResponseEntity<Object> cadastrarComissao(
            @RequestBody CadastroComissaoDTO cadastroComissaoDTO
    ) {
        Comissao comissao = new Comissao();
        BeanUtils.copyProperties(cadastroComissaoDTO, comissao);
        comissaoService.save(comissao);
        return ResponseEntity.status(HttpStatus.CREATED).body(comissao);
    }

    @GetMapping()
    ResponseEntity<Object> findAll() {
        List<Comissao> comissaoList = comissaoService.findAll();
        if (comissaoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma comissão encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(comissaoList);
    }

    @GetMapping("/{idComissao}")
    ResponseEntity<Object> findByIdComissao(
            @RequestBody Integer idComissao
    ) {
        if (!comissaoService.existsById(idComissao)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comissão com o id: " + idComissao + " não existe!");
        }
        Comissao comissao = comissaoService.findById(idComissao).get();
        if (comissao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma comissão encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(comissao);
    }

    @PutMapping("/{idComissao}")
    ResponseEntity<Object> update(
            @RequestBody CadastroComissaoDTO cadastroComissaoDTO,
            @PathVariable Integer idComissao
    ) {
        if (!comissaoService.existsById(idComissao)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comissão com o id: " + idComissao + " não existe!");
        }
        Comissao comissao = new Comissao();
        BeanUtils.copyProperties(cadastroComissaoDTO, comissao);
        comissao.setIdComissao(idComissao);
        comissaoService.save(comissao);
        return ResponseEntity.status(HttpStatus.OK).body(comissao);
    }

}
