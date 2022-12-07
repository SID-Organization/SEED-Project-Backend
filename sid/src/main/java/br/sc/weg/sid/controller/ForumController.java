package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroForumDTO;
import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.service.ForumService;
import br.sc.weg.sid.model.service.UsuarioService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Object> cadastroForum(@RequestBody CadastroForumDTO cadadastroForumDTO) {
        Forum forum = new Forum();
        BeanUtils.copyProperties(cadadastroForumDTO, forum);

        Forum forumSalvo = forumService.save(forum);
        return ResponseEntity.ok("Fórum cadastrado com sucesso! \n" + forumSalvo);
    }

    @GetMapping
    public ResponseEntity<Object> listarForuns() {
        try {
            return ResponseEntity.ok(forumService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar fóruns: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarForumPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(forumService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar fórum: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarForum(@PathVariable Integer id) {
        try {
            forumService.deleteById(id);
            return ResponseEntity.ok("Fórum deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar fórum: " + e.getMessage());
        }
    }

    @GetMapping("/analista/{id}")
    public ResponseEntity<Object> listarForumPorAnalista(@PathVariable Integer id) {
        try {
            Usuario analista = usuarioService.findById(id).get();
            return ResponseEntity.ok((forumService.findByNumeroCadastroAnalistaResponsavel(analista)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar fóruns: " + e.getMessage());
        }
    }

}
