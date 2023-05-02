package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroComissaoDTO;
import br.sc.weg.sid.model.entities.Comissao;
import br.sc.weg.sid.model.service.ComissaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sid/api/comissao")
public class ComissaoController {

    @Autowired
    ComissaoService comissaoService;

    /**
     * Endpoint para cadastrar uma nova comissão.
     *
     * @param cadastroComissaoDTO objeto DTO contendo as informações da comissão a ser cadastrada.
     * @return ResponseEntity com status HTTP 201 (Created) e a comissão cadastrada no corpo da resposta.
     */
    @PostMapping()
    ResponseEntity<Object> cadastrarComissao(
            @RequestBody CadastroComissaoDTO cadastroComissaoDTO
    ) {
        Comissao comissao = new Comissao();
        BeanUtils.copyProperties(cadastroComissaoDTO, comissao);
        comissaoService.save(comissao);
        return ResponseEntity.status(HttpStatus.CREATED).body(comissao);
    }

    /**
     * Endpoint para listar todas as comissões cadastradas no sistema.
     *
     * @return ResponseEntity com status HTTP 200 (OK) e uma lista de comissões no corpo da resposta,
     * ou status HTTP 404 (Not Found) caso não exista nenhuma comissão cadastrada.
     */
    @GetMapping()
    ResponseEntity<Object> findAll() {
        List<Comissao> comissaoList = comissaoService.findAll();
        if (comissaoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma comissão encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(comissaoList);
    }

    /**
     * Endpoint para buscar uma comissão específica pelo seu ID.
     *
     * @param idComissao o ID da comissão a ser buscada no banco de dados, id passado pela URL.
     * @return ResponseEntity com status HTTP 200 (OK) e a comissão encontrada no corpo da resposta, ou status HTTP 404 (Not Found) caso a comissão não exista.
     */
    @GetMapping("/{idComissao}")
    ResponseEntity<Object> findByIdComissao(
            @RequestBody Integer idComissao
    ) {
        if (!comissaoService.existsById(idComissao)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comissão com o id: " + idComissao + " não existe!");
        }
        Comissao comissao = comissaoService.findById(idComissao).get();
        if (comissao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma comissão encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(comissao);
    }

    /**
     * Endpoint para atualizar os dados de uma comissão existente.
     *
     * @param cadastroComissaoDTO objeto DTO contendo as informações atualizadas da comissão.
     * @param idComissao          o ID da comissão a ser atualizada passado pela URL.
     * @return ResponseEntity com status HTTP 200 (OK) e a comissão atualizada no corpo da resposta, ou status HTTP 404 (Not Found) caso a comissão não exista.
     */
    @PutMapping("/{idComissao}")
    ResponseEntity<Object> update(
            @RequestBody CadastroComissaoDTO cadastroComissaoDTO,
            @PathVariable Integer idComissao
    ) {
        if (!comissaoService.existsById(idComissao)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comissão com o id: " + idComissao + " não existe!");
        }
        Comissao comissao = new Comissao();
        BeanUtils.copyProperties(cadastroComissaoDTO, comissao);
        comissao.setIdComissao(idComissao);
        comissaoService.save(comissao);
        return ResponseEntity.status(HttpStatus.OK).body(comissao);
    }

}
