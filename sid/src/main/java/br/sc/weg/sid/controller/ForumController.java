package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroForumDTO;
import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.service.ForumService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sid/api/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<Object> cadastroForum(@RequestBody CadastroForumDTO cadadastroForumDTO) {
        Forum forum = new Forum();
        BeanUtils.copyProperties(cadadastroForumDTO, forum);

        Forum forumSalvo = forumService.save(forum);
        return ResponseEntity.ok("Fórum cadastrado com sucesso! \n" + forumSalvo);
    }

}
