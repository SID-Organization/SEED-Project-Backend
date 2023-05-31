package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroSecaoTIResponsavelDTO;
import br.sc.weg.sid.model.entities.SecaoTIResponsavel;
import br.sc.weg.sid.model.service.SecaoTIResponsavelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sid/api/secao-ti-responsavel")
public class SecaoTIResponsavelController {

    @Autowired
    SecaoTIResponsavelService secaoTIResponsavelService;

    /**
     * Esta função é um mapeamento de requisição HTTP POST para o cadastro de uma nova seção de TI responsável.
     * @param cadastroSecaoTIResponsavelDTO este objeto DTO contém os dados necessários para o cadastro de uma nova seção de TI responsável.
     * @return Se a operação for bem sucedida, retorna um objeto ResponseEntity com o status HTTP 201 CREATED e o objeto SecaoTIResponsavel criado.
     * Caso contrário, retorna um objeto ResponseEntity com o status HTTP 400 BAD REQUEST e uma mensagem de erro.
     */
    @PostMapping()
    ResponseEntity<Object> cadastrarSecaoTIResponsavel(
            @RequestBody CadastroSecaoTIResponsavelDTO cadastroSecaoTIResponsavelDTO
    ) {
        SecaoTIResponsavel secaoTIResponsavel = new SecaoTIResponsavel();
        BeanUtils.copyProperties(cadastroSecaoTIResponsavelDTO, secaoTIResponsavel);
        secaoTIResponsavelService.save(secaoTIResponsavel);

        return ResponseEntity.status(HttpStatus.CREATED).body(secaoTIResponsavel);
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET para a busca de uma seção de TI responsável por ID.
     * @param id este parâmetro representa o ID da seção de TI responsável que será buscada.
     * @return Se a operação for bem sucedida, retorna um objeto ResponseEntity com o status HTTP 200 OK e o objeto SecaoTIResponsavel buscado.
     * Caso contrário, retorna um objeto ResponseEntity com o status HTTP 400 BAD REQUEST e uma mensagem de erro.
     */
    @GetMapping("/{id}")
    ResponseEntity<Object> buscarSecaoTIResponsavel(
            @PathVariable Integer id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(secaoTIResponsavelService.findById(id));
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET para a busca de todas as seções de TI responsáveis.
     * @return Se a operação for bem sucedida, retorna um objeto ResponseEntity com o status HTTP 200 OK e uma lista de objetos SecaoTIResponsavel.
     * Caso contrário, retorna um objeto ResponseEntity com o status HTTP 400 BAD REQUEST e uma mensagem de erro.
     */
    @GetMapping()
    ResponseEntity<Object> buscarSecaoTIResponsavel() {
        return ResponseEntity.status(HttpStatus.OK).body(secaoTIResponsavelService.findAll());
    }

    /**
     * Esta função é um mapeamento de requisição HTTP PUT para a atualização dos dados de uma seção de TI responsável.
     * @param id este parâmetro representa o ID da seção de TI responsável que será atualizada.
     * @param cadastroSecaoTIResponsavelDTO este objeto DTO contém os dados necessários para a atualização da seção de TI responsável.
     * @return Se a operação for bem sucedida, retorna um objeto ResponseEntity com o status HTTP 200 OK e o objeto SecaoTIResponsavel atualizado.
     * Caso contrário, retorna um objeto ResponseEntity com o status HTTP 400 BAD REQUEST e uma mensagem de erro.
     */
    @PutMapping("/{id}")
    ResponseEntity<Object> atualizarSecaoTIResponsavel(
            @PathVariable Integer id,
            @RequestBody CadastroSecaoTIResponsavelDTO cadastroSecaoTIResponsavelDTO
    ) {
        SecaoTIResponsavel secaoTIResponsavel = new SecaoTIResponsavel();
        BeanUtils.copyProperties(cadastroSecaoTIResponsavelDTO, secaoTIResponsavel);
        secaoTIResponsavel.setIdSecaoTIResponsavel(id);
        secaoTIResponsavelService.save(secaoTIResponsavel);

        return ResponseEntity.status(HttpStatus.OK).body(secaoTIResponsavel);
    }
}
