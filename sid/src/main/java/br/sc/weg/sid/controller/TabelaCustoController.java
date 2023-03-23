package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroChatDTO;
import br.sc.weg.sid.DTO.CadastroTabelaCustoDTO;
import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.TabelaCusto;
import br.sc.weg.sid.model.service.TabelaCustoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/tabela-custo")
public class TabelaCustoController {

    @Autowired
    TabelaCustoService tabelaCustoService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody CadastroTabelaCustoDTO cadastroTabelaCustoDTO) {
        TabelaCusto tabelaCusto = new TabelaCusto();
        BeanUtils.copyProperties(cadastroTabelaCustoDTO, tabelaCusto);
        try {
            tabelaCustoService.save(tabelaCusto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Tabela de custo não salva! Message: " + e.getMessage());
        }
        return ResponseEntity.ok().body("Tabela de custo salva com sucesso! \n" + tabelaCusto.toString());
    }

    @GetMapping()
    ResponseEntity<Object> listarTabelaCusto() {
        return ResponseEntity.ok().body(tabelaCustoService.findAll());
    }


}
