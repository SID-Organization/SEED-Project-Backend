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

    /**
     * Injeção de dependência para utilizar os métodos da classe BeneficioService.
     */
    BeneficioService beneficioService;


    /**
     * Deleta um benefício pelo seu id.
     *
     * @param idBeneficio o id do benefício a ser deletado, passado na URL.
     * @return um ResponseEntity com uma mensagem de sucesso se o benefício foi deletado, ou com uma mensagem de erro caso contrário.
     */
    @DeleteMapping("/{idBeneficio}")
    public ResponseEntity<Object> deleteById(@PathVariable Integer idBeneficio) {
        if (beneficioService.existsById(idBeneficio)) {
            beneficioService.deleteById(idBeneficio);
            return ResponseEntity.ok().body("Benefício com o id: " + idBeneficio + " deletado com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("ERROR 0006: O benefício inserido não existe! ID BENEFÍCIO: " + idBeneficio);
        }
    }

}
