package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.service.PropostaService;
import br.sc.weg.sid.model.service.TabelaCustoLinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/tabela-custo")
public class TabelaCustoController {

    @Autowired
    TabelaCustoLinhaService tabelaCustoLinhaService;

    @Autowired
    PropostaService propostaService;

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna todas as tabelas de custo cadastradas no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo todas as tabelas de custo cadastradas no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não exista tabelas de custo cadastradas no banco de dados.
     */
    @GetMapping()
    ResponseEntity<Object> listarTabelaCusto() {
        try {
            return ResponseEntity.ok().body(tabelaCustoLinhaService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Não existe tabela de custo");
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna uma tabela de custo cadastrada no banco de dados.
     * @param id - Parâmetro que representa o id da tabela de custo.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo a tabela de custo cadastrada no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não exista uma tabela de custo cadastrada no banco de dados com o id informado.
     */
    @GetMapping("/{id}")
    ResponseEntity<Object> listarTabelaCustoPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(tabelaCustoLinhaService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Não existe tabela de custo com id: " + id);
        }
    }

}
