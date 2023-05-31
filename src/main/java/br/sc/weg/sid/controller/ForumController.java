package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroForumDTO;
import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.service.ForumService;
import br.sc.weg.sid.model.service.UsuarioService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Esta função é um mapeamento de requisição HTTP POST que recebe um objeto CadastroForumDTO e retorna um objeto ResponseEntity com status 201 e o corpo contendo o objeto Forum salvo.
     * @param cadastroForumDTO - Parâmetro que representa um objeto DTO com os dados do fórum a ser salvo.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 201 e o corpo contendo o objeto Forum salvo.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível salvar o objeto Forum.
     */
    @PostMapping
    public ResponseEntity<Object> cadastroForum(@RequestBody CadastroForumDTO cadastroForumDTO) {
        Forum forum = new Forum();
        BeanUtils.copyProperties(cadastroForumDTO, forum);

        Forum forumSalvo = forumService.save(forum);
        return ResponseEntity.status(HttpStatus.CREATED).body(forumSalvo);
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna todos os objetos Forum salvos no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma lista de objetos Forum.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível listar os objetos Forum.
     */
    @GetMapping
    public ResponseEntity<Object> listarForuns() {
        try {
            return ResponseEntity.ok(forumService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar fóruns: " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna o objeto Forum de acordo com o id informado.
     * @param id - Parâmetro que representa o id do objeto Forum.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o objeto Forum.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível listar o objeto Forum.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> listarForumPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(forumService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar fórum: " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP DELETE que deleta o objeto Forum de acordo com o id informado.
     * @param id - Parâmetro que representa o id do objeto Forum.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma mensagem de sucesso.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível deletar o objeto Forum.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarForum(@PathVariable Integer id) {
        try {
            forumService.deleteById(id);
            return ResponseEntity.ok("Fórum deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar fórum: " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna todos os objetos Forum de acordo com o id do analista informado.
     * @param id - Parâmetro que representa o id do analista que queremos listar os fóruns.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma lista de objetos Forum.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível listar os objetos Forum.
     */
    @GetMapping("/analista/{id}")
    public ResponseEntity<Object> listarForumPorAnalista(@PathVariable Integer id) {
        try {
            Usuario analista = usuarioService.findById(id).get();
            return ResponseEntity.ok((forumService.findByAnalistaResponsavelForum(analista)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar fóruns: " + e.getMessage());
        }
    }

}
