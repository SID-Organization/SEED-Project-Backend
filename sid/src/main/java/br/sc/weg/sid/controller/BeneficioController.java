package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.service.BeneficioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/beneficio")
@AllArgsConstructor
public class BeneficioController {

    BeneficioService beneficioService;

    @DeleteMapping("/{idBeneficio}")
    public ResponseEntity<Object> deleteById(@PathVariable Integer idBeneficio){
        if (beneficioService.existsById(idBeneficio)){
            beneficioService.deleteById(idBeneficio);
            return ResponseEntity.ok().body("Benefício com o id: " + idBeneficio + " deletado com sucesso!");
        }else {
            return ResponseEntity.badRequest().body("ERROR 0006: O benefício inserido não existe! ID BENEFÍCIO: " + idBeneficio);
        }
    }

}
