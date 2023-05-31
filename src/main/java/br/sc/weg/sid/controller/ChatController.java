package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroChatDTO;
import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.ChatResumido;
import br.sc.weg.sid.model.service.ChatService;
import br.sc.weg.sid.model.service.MensagemService;
import br.sc.weg.sid.utils.ChatUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MensagemService mensagemService;

    ChatUtil chatUtil = new ChatUtil();

    /**
     * Endpoint para salvar um chat.
     *
     * @param cadastroChatDTO o DTO com os dados do chat a ser salvo.
     * @return um ResponseEntity com a resposta da operação.
     */
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

    /**
     * Endpoint para buscar um chat por ID.
     *
     * @param idChat o ID do chat a ser buscado.
     * @return um ResponseEntity com o chat encontrado ou uma mensagem de erro caso não seja encontrado.
     */
    @GetMapping("/{idChat}")
    public ResponseEntity<Object> findById(@PathVariable Integer idChat) {
        Optional<Chat> chatOptional = chatService.findById(idChat);
        if (chatOptional == null) {
            return ResponseEntity.badRequest().body("Chat não encontrado!");
        }
        return ResponseEntity.ok().body(chatOptional.get());
    }

    /**
     * Endpoint para buscar todos os chats.
     *
     * @return um ResponseEntity com a lista de todos os chats ou uma mensagem de erro caso não haja chats.
     */
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok().body(chatService.findAll());
    }

    /**
     * Endpoint para buscar chats por número de cadastro do usuário.
     *
     * @param numeroCadastroUsuario o número de cadastro do usuário dono do chat.
     * @return um ResponseEntity com uma lista resumida dos chats do usuário ou uma mensagem de erro caso não haja chats.
     * @throws ParseException caso ocorra um erro na formatação de datas.
     */
    @GetMapping("/usuario/{numeroCadastroUsuario}")
    public ResponseEntity<Object> findChatByNumeroCadastroUsuario(@PathVariable Integer numeroCadastroUsuario) throws ParseException {
        return ResponseEntity.ok().body(chatUtil.resumirChat(chatService.findChatByNumeroCadastroUsuario(numeroCadastroUsuario), mensagemService, numeroCadastroUsuario));
    }

    /**
     * Endpoint para buscar mensagens de um chat por ID.
     *
     * @param idChat o ID do chat do qual buscar as mensagens.
     * @return um ResponseEntity com uma lista resumida das mensagens do chat ou uma mensagem de erro caso não haja mensagens.
     */
    @GetMapping("/mensagem/{idChat}")
    public ResponseEntity<Object> findMensagemByIdChat(@PathVariable Integer idChat) {
        Chat chat = new Chat();
        ChatResumido chatResumido = new ChatResumido();
        chatService.findById(idChat).ifPresent(chat1 -> chat.setIdChat(chat1.getIdChat()));
        return ResponseEntity.ok().body(chatUtil.resumirMensagem(mensagemService.findByIdChat(chat)));
    }

}
