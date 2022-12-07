package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Mensagem;
import br.sc.weg.sid.model.service.ChatService;
import br.sc.weg.sid.model.service.MensagemService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin
public class MensagemController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/sid/api/mensagem")
    public ResponseEntity<Object> receiveMessage(@RequestBody Mensagem mensagem) {
        // /demanda/{idDemanda}/{idChat}
        simpMessagingTemplate.convertAndSendToUser( /*id da demanda*/ mensagem.getIdChat().getIdDemanda().getIdDemanda().toString(), /*id do chat*/ mensagem.getIdChat().getIdChat().toString(), mensagem);
        System.out.println(mensagem.toString());
        try {
            mensagemService.save(mensagem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Mensagem não salva! Message: " + e.getMessage());
        }
        return ResponseEntity.ok().body("Mensagem enviada com sucesso!");
    }

}
