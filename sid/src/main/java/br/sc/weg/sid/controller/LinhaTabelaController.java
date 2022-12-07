package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroLinhaTabelaDTO;
import br.sc.weg.sid.model.entities.LinhaTabela;
import br.sc.weg.sid.model.entities.TabelaCusto;
import br.sc.weg.sid.model.service.LinhaTabelaService;
import br.sc.weg.sid.model.service.TabelaCustoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/linha-tabela-custo")
public class LinhaTabelaController {
    @Autowired
    private LinhaTabelaService linhaTabelaService;

    @Autowired
    private TabelaCustoService tabelaCustoService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CadastroLinhaTabelaDTO cadastroLinhaTabelaDTO) {
        LinhaTabela linhaTabela = new LinhaTabela();
        BeanUtils.copyProperties(cadastroLinhaTabelaDTO, linhaTabela);
        try {
            LinhaTabela linhaTabelaSalva = linhaTabelaService.save(linhaTabela);
            return ResponseEntity.status(HttpStatus.CREATED).body(linhaTabelaSalva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(linhaTabelaService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(linhaTabelaService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/tabela-custo/{id}")
    public ResponseEntity<Object> findAllByIdTabelaCusto(@PathVariable Integer id) {
        try {
            TabelaCusto tabelaCusto = tabelaCustoService.findById(id).get();
            return ResponseEntity.status(HttpStatus.OK).body(linhaTabelaService.findAllByIdTabelaCusto(tabelaCusto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Integer id) {
        try {
            linhaTabelaService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Linha de tabela de custo excluída com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
