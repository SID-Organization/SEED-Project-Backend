package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroSecaoTIResponsavelDTO;
import br.sc.weg.sid.model.entities.SecaoTIResponsavel;
import br.sc.weg.sid.model.service.SecaoTIResponsavelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sid/api/secao-ti-responsavel")
public class SecaoTIResponsavelController {

    @Autowired
    SecaoTIResponsavelService secaoTIResponsavelService;

    @PostMapping()
    ResponseEntity<Object> cadastrarSecaoTIResponsavel(
            @RequestBody CadastroSecaoTIResponsavelDTO cadastroSecaoTIResponsavelDTO
    ) {
        SecaoTIResponsavel secaoTIResponsavel = new SecaoTIResponsavel();
        BeanUtils.copyProperties(cadastroSecaoTIResponsavelDTO, secaoTIResponsavel);
        secaoTIResponsavelService.save(secaoTIResponsavel);

        return ResponseEntity.status(HttpStatus.CREATED).body(secaoTIResponsavel);
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> buscarSecaoTIResponsavel(
            @PathVariable Integer id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(secaoTIResponsavelService.findById(id));
    }

    @GetMapping()
    ResponseEntity<Object> buscarSecaoTIResponsavel() {
        return ResponseEntity.status(HttpStatus.OK).body(secaoTIResponsavelService.findAll());
    }

    @PutMapping("/{id}")
    ResponseEntity<Object> atualizarSecaoTIResponsavel(
            @PathVariable Integer id,
            @RequestBody CadastroSecaoTIResponsavelDTO cadastroSecaoTIResponsavelDTO
    ) {
        SecaoTIResponsavel secaoTIResponsavel = new SecaoTIResponsavel();
        BeanUtils.copyProperties(cadastroSecaoTIResponsavelDTO, secaoTIResponsavel);
        secaoTIResponsavel.setIdSecaoTIResponsavel(id);
        secaoTIResponsavelService.save(secaoTIResponsavel);

        return ResponseEntity.status(HttpStatus.OK).body(secaoTIResponsavel);
    }
}
