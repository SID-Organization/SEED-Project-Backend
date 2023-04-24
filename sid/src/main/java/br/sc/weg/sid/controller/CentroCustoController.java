package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroCentroCustoDTO;
import br.sc.weg.sid.model.entities.CentroCusto;
import br.sc.weg.sid.model.service.CentroCustoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/centro-custo")
public class CentroCustoController {

    @Autowired
    private CentroCustoService centroCustoService;

    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody CadastroCentroCustoDTO cadastroCentroCustoDTO) {
        try {
            CentroCusto centroCusto = new CentroCusto();
            BeanUtils.copyProperties(cadastroCentroCustoDTO, centroCusto);
            centroCustoService.save(centroCusto);
            return ResponseEntity.status(HttpStatus.CREATED).body(centroCusto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar centro de custo: " + e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<Object> findAll() {
        List<CentroCusto> centroCustoList = centroCustoService.findAll();
        if (centroCustoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum centro de custo encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoList);
    }

    @GetMapping("/id/{idCentroCusto}")
    public ResponseEntity<Object> findByIdCentroCusto(@PathVariable Integer idCentroCusto) {
        if(!centroCustoService.existsById(idCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centro de custo com o id: " + idCentroCusto + " não existe!");
        }
        CentroCusto centroCusto = centroCustoService.findById(idCentroCusto).get();
        if (centroCusto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum centro de custo encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(centroCusto);

    }

    @PutMapping("/{idCentroCusto}")
    public ResponseEntity<Object> update(@PathVariable Integer idCentroCusto,@RequestBody CadastroCentroCustoDTO centroCustoDTO) {
        if (!centroCustoService.existsById(idCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centro de custo com o id: " +idCentroCusto + " não encontrado");
        }
        Optional<CentroCusto> centroCustoOptional = centroCustoService.findById(idCentroCusto);
        CentroCusto centroCusto = centroCustoOptional.get();
        BeanUtils.copyProperties(centroCustoDTO, centroCusto);
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoService.save(centroCusto));
    }

    @DeleteMapping("/{idCentroCusto}")
    public ResponseEntity<Object> delete(@PathVariable Integer idCentroCusto) {
        if (!centroCustoService.existsById(idCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centro de custo não encontrado");
        }
        centroCustoService.deleteById(idCentroCusto);
        return ResponseEntity.status(HttpStatus.OK).body("Centro de custo com id " + idCentroCusto +" deletado com sucesso!");
    }
}
