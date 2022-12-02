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
public class MensagemController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @CrossOrigin(origins = "http://localhost:3000")
    @MessageMapping("/sid/api/mensagem")
    public ResponseEntity<Object> receiveMessage(@RequestBody Mensagem mensagem) {
        simpMessagingTemplate.convertAndSendToUser(mensagem.getIdChat().getIdDemanda().getIdDemanda().toString(), mensagem.getIdChat().getIdChat().toString(), mensagem);
        System.out.println(mensagem.toString());
        try {
            mensagemService.save(mensagem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Mensagem não enviada! Message: " + e.getMessage());
        }
        return ResponseEntity.ok().body("Mensagem enviada com sucesso!");
    }

}
