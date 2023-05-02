package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroNotificacaoDTO;
import br.sc.weg.sid.model.entities.Notificacao;
import br.sc.weg.sid.model.service.NotificacaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/notificacao")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    /**
     * Esta funcao é um mapeamento de requisição HTTP POST que salva um objeto Notificacao no banco de dados.
     * @param cadastroNotificacaoDTO - Objeto DTO que contém os dados da Notificacao a ser salva.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 201 e o corpo contendo o objeto Notificacao salvo.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível salvar o objeto Notificacao.
     */
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CadastroNotificacaoDTO cadastroNotificacaoDTO) {
        Notificacao notificacao = new Notificacao();
        BeanUtils.copyProperties(cadastroNotificacaoDTO, notificacao);
        try {
            Notificacao notificacaoSalva = notificacaoService.save(notificacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(notificacaoSalva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Esta funcao é um mapeamento de requisição HTTP GET que retorna todos os objetos Notificacao salvos no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma lista de objetos Notificacao.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível retornar os objetos Notificacao.
     */
    @GetMapping
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(notificacaoService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Esta funcao é um mapeamento de requisição HTTP GET que retorna o objeto Notificacao de acordo com o id informado.
     * @param id - Parâmetro que representa o id do objeto Notificacao.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o objeto Notificacao.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível retornar o objeto Notificacao.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(notificacaoService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Esta funcao é um mapeamento de requisição HTTP DELETE que exclui o objeto Notificacao de acordo com o id informado.
     * @param id - Parâmetro que representa o id do objeto Notificacao.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma mensagem de sucesso.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível excluir o objeto Notificacao.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Integer id) {
        try {
            notificacaoService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Notificação excluída com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
