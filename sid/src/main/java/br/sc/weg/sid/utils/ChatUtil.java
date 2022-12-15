package br.sc.weg.sid.utils;

import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.ChatResumido;
import br.sc.weg.sid.model.entities.Mensagem;
import br.sc.weg.sid.model.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatUtil {

    public List<ChatResumido> resumirChat(List<Chat> chatList, MensagemService mensagemService) {
        List<ChatResumido> chatResumidoList = new ArrayList<>();
        for (Chat chat : chatList) {
            if (chat != null) {
                ChatResumido chatResumido = new ChatResumido();
                Integer idAnalista = null;
                List<Mensagem> mensagens = null;
                System.out.println(chat);
                chatResumido.setTituloDemanda(chat.getIdDemanda().getTituloDemanda());
                for (int i = 0; i < chat.getUsuarios().size(); i++) {
                    if (chat.getUsuarios().get(i).getNumeroCadastroUsuario() != chat.getIdDemanda().getSolicitanteDemanda().getNumeroCadastroUsuario()) {
                        chatResumido.setFotoAnalista(chat.getUsuarios().get(i).getFotoUsuario());
                        chatResumido.setNomeAnalista(chat.getUsuarios().get(i).getNomeUsuario());
                        idAnalista = chat.getUsuarios().get(i).getNumeroCadastroUsuario();
                        mensagens = mensagemService.findByIdChatAndIdUsuario(chat, chat.getUsuarios().get(i));
                    }
                }

                Integer idMensagem = 0;
                for (Mensagem mensagem : mensagens) {
                    if (mensagem.getIdMensagem() > idMensagem) {
                        idMensagem = mensagem.getIdMensagem();
                    }
                }

                for (Mensagem mensagem : mensagens) {
                    if (mensagem.getIdMensagem() == idMensagem) {
                        chatResumido.setUltimaMensagem(mensagem.getTextoMensagem());
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        chatResumido.setDataUltimaMensagem(sdf.format(mensagem.getDataMensagem()));
                    }
                }
                chatResumidoList.add(chatResumido);
            }
        }
        return chatResumidoList;
    }
}
