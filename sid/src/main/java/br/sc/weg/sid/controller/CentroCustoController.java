package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.CentroCusto;
import br.sc.weg.sid.model.service.CentroCustoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/sid/api/centro-custo")
public class CentroCustoController {

    @Autowired
    private CentroCustoService centroCustoService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        List<CentroCusto> centroCustos = centroCustoService.findAll();
        if (centroCustos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum centro de custo encontrado!");
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(centroCustos);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/nome/{nomeCentroCusto}")
    public ResponseEntity<Object> findByNomeCentroCusto(@PathVariable String nomeCentroCusto) {
        CentroCusto centroCusto = centroCustoService.findByNomeCentroCusto(nomeCentroCusto).get();
        if (centroCusto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum centro de custo encontrado!");
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(centroCusto);
    }

}
