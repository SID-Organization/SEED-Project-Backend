package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroChatDTO;
import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.service.ChatService;
import org.apache.coyote.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody CadastroChatDTO cadastroChatDTO){
        Chat chat = new Chat();
        BeanUtils.copyProperties(cadastroChatDTO, chat);
        try {
            chatService.save(chat);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Chat não salvo! Message: " + e.getMessage());
        }
        return ResponseEntity.ok().body("Chat salvo com sucesso! \n" + chat.toString());
    }

    @GetMapping("/{idChat}")
    public ResponseEntity<Object> findById(@PathVariable Integer idChat){
        Optional<Chat> chatOptional = chatService.findById(idChat);
        if(chatOptional == null){
            return ResponseEntity.badRequest().body("Chat não encontrado!");
        }
        return ResponseEntity.ok().body(chatOptional.get());
    }

    @GetMapping
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok().body(chatService.findAll());
    }
}
