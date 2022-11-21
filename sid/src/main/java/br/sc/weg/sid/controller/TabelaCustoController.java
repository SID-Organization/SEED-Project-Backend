package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroTabelaCustoDTO;
import br.sc.weg.sid.model.entities.TabelaCusto;
import br.sc.weg.sid.model.service.TabelaCustoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/sid/api/tabela-custo")
public class TabelaCustoController {

    @Autowired
    private TabelaCustoService tabelaCustoService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CadastroTabelaCustoDTO cadastroTabelaCustoDTO) {
        TabelaCusto tabelaCusto = new TabelaCusto();
        BeanUtils.copyProperties(cadastroTabelaCustoDTO, tabelaCusto);
        try {
            TabelaCusto tabelaCustoSalva = tabelaCustoService.save(tabelaCusto);
            return ResponseEntity.status(HttpStatus.CREATED).body(tabelaCustoSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar tabela de custo" + "\n" + e.getMessage());
        }
    }
}
