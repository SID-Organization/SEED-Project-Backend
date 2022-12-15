package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroChatDTO;
import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.ChatResumido;
import br.sc.weg.sid.model.service.ChatService;
import br.sc.weg.sid.model.service.MensagemService;
import br.sc.weg.sid.utils.ChatUtil;
import org.apache.coyote.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MensagemService mensagemService;

    ChatUtil chatUtil = new ChatUtil();

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody CadastroChatDTO cadastroChatDTO) {
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
    public ResponseEntity<Object> findById(@PathVariable Integer idChat) {
        Optional<Chat> chatOptional = chatService.findById(idChat);
        if (chatOptional == null) {
            return ResponseEntity.badRequest().body("Chat não encontrado!");
        }
        return ResponseEntity.ok().body(chatOptional.get());
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok().body(chatService.findAll());
    }

    @GetMapping("/usuario/{numeroCadastroUsuario}")
    public ResponseEntity<Object> findChatByNumeroCadastroUsuario(@PathVariable Integer numeroCadastroUsuario) throws ParseException {
        System.out.println(chatService.findChatByNumeroCadastroUsuario(numeroCadastroUsuario));
        return ResponseEntity.ok().body(chatUtil.resumirChat(chatService.findChatByNumeroCadastroUsuario(numeroCadastroUsuario), mensagemService, numeroCadastroUsuario));
    }

    @GetMapping("/mensagem/{idChat}")
    public ResponseEntity<Object> findMensagemByIdChat(@PathVariable Integer idChat) {
        Chat chat = new Chat();
        ChatResumido chatResumido = new ChatResumido();
        chatService.findById(idChat).ifPresent(chat1 -> chat.setIdChat(chat1.getIdChat()));
        return ResponseEntity.ok().body(chatUtil.resumirMensagem(mensagemService.findByIdChat(chat)));
    }
}
