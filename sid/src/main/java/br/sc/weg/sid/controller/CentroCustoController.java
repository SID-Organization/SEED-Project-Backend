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

    /**
     * Injeção de dependência para utilizar os métodos da classe CentroCustoService.
     */
    @Autowired
    private CentroCustoService centroCustoService;


    /**
     * Salva um novo Centro de Custo no banco de dados.
     *
     * @param cadastroCentroCustoDTO DTO com as informações do Centro de Custo a ser salvo.
     * @return ResponseEntity com o Centro de Custo salvo ou mensagem de erro.
     */
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

    /**
     * Retorna uma lista com todos os Centros de Custos cadastrados no banco de dados.
     *
     * @return ResponseEntity com a lista de Centros de Custos ou mensagem de erro caso não haja nenhum Centro de Custo cadastrado.
     */
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        List<CentroCusto> centroCustoList = centroCustoService.findAll();
        if (centroCustoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum centro de custo encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoList);
    }

    /**
     * Busca um Centro de Custo no banco de dados a partir do seu ID.
     *
     * @param idCentroCusto ID do Centro de Custo a ser buscado, passado como parâmetro na URL.
     * @return ResponseEntity com o Centro de Custo encontrado ou mensagem de erro caso o ID não exista ou não seja encontrado nenhum Centro de Custo.
     */
    @GetMapping("/id/{idCentroCusto}")
    public ResponseEntity<Object> findByIdCentroCusto(@PathVariable Integer idCentroCusto) {
        if (!centroCustoService.existsById(idCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centro de custo com o id: " + idCentroCusto + " não existe!");
        }
        CentroCusto centroCusto = centroCustoService.findById(idCentroCusto).get();
        if (centroCusto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum centro de custo encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(centroCusto);
    }

    /**
     * Atualiza as informações de um Centro de Custo existente no banco de dados.
     *
     * @param idCentroCusto  ID do Centro de Custo a ser atualizado no banco de dados, id passado na URL.
     * @param centroCustoDTO DTO com as informações atualizadas do Centro de Custo.
     * @return ResponseEntity com o Centro de Custo atualizado ou mensagem de erro caso o ID não exista.
     */
    @PutMapping("/{idCentroCusto}")
    public ResponseEntity<Object> update(@PathVariable Integer idCentroCusto, @RequestBody CadastroCentroCustoDTO centroCustoDTO) {
        if (!centroCustoService.existsById(idCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centro de custo com o id: " + idCentroCusto + " não encontrado");
        }
        Optional<CentroCusto> centroCustoOptional = centroCustoService.findById(idCentroCusto);
        CentroCusto centroCusto = centroCustoOptional.get();
        BeanUtils.copyProperties(centroCustoDTO, centroCusto);
        return ResponseEntity.status(HttpStatus.OK).body(centroCustoService.save(centroCusto));
    }


    /**
     * Exclui um Centro de Custo pelo seu ID.
     *
     * @param idCentroCusto o ID do Centro de Custo a ser excluído, passado como parâmetro na URL.
     * @return ResponseEntity com mensagem de sucesso se a operação for bem-sucedida ou mensagem de erro se o Centro de Custo não for encontrado.
     */
    @DeleteMapping("/{idCentroCusto}")
    public ResponseEntity<Object> delete(@PathVariable Integer idCentroCusto) {
        if (!centroCustoService.existsById(idCentroCusto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centro de custo não encontrado");
        }
        centroCustoService.deleteById(idCentroCusto);
        return ResponseEntity.status(HttpStatus.OK).body("Centro de custo com id " + idCentroCusto + " deletado com sucesso!");
    }
}
