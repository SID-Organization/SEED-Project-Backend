package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.MensagemDTO;
import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.Mensagem;
import br.sc.weg.sid.model.service.ChatService;
import br.sc.weg.sid.model.service.MensagemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MensagemController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Esta funcao é um mapeamento de requisição WebSocket que recebe uma mensagem e a envia para o chat correspondente.
     * @param mensagemDTO - Parâmetro que representa o objeto MensagemDTO, que contém os dados da mensagem a ser enviada.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma mensagem de sucesso.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível enviar a mensagem.
     */
    @MessageMapping("/sid/api/mensagem")
    public ResponseEntity<Object> receiveMessage(@RequestBody MensagemDTO mensagemDTO){
        Mensagem mensagem = new Mensagem();
        BeanUtils.copyProperties(mensagemDTO, mensagem);
        // /demanda/{idDemanda}/{idChat}
        Chat chat = chatService.findById(mensagemDTO.getIdChat().getIdChat()).get();
        simpMessagingTemplate.convertAndSendToUser( /*id da demanda*/ chat.getIdDemanda().getIdDemanda().toString(), /*id do chat*/ mensagem.getIdChat().getIdChat().toString(), mensagem);
        try {
            mensagemService.save(mensagem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Mensagem não salva! Message: " + e.getMessage());
        }
        return ResponseEntity.ok().body("Mensagem enviada com sucesso!");
    }

}
