package br.sc.weg.sid.utils;

import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.ChatResumido;
import br.sc.weg.sid.model.entities.Mensagem;
import br.sc.weg.sid.model.entities.MensagemResumida;
import br.sc.weg.sid.model.service.MensagemService;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatUtil {

    public List<ChatResumido> resumirChat(List<Chat> chatList, MensagemService mensagemService, Integer numeroCadastroUsuario) {
        List<ChatResumido> chatResumidoList = new ArrayList<>();
        for (Chat chat : chatList) {
            if (chat != null) {
                ChatResumido chatResumido = new ChatResumido();
                Integer idAnalista;
                List<Mensagem> mensagens = null;
                chatResumido.setTituloDemanda(chat.getIdDemanda().getTituloDemanda());
                chatResumido.setIdChat(chat.getIdChat());
                chatResumido.setIdDemanda(chat.getIdDemanda().getIdDemanda());
                for (int i = 0; i < chat.getUsuarios().size(); i++) {
                    if (!chat.getUsuarios().get(i).getNumeroCadastroUsuario().equals(numeroCadastroUsuario)) {
                        chatResumido.setFotoAnalista(chat.getUsuarios().get(i).getFotoUsuario());
                        chatResumido.setNomeAnalista(chat.getUsuarios().get(i).getNomeUsuario());
                        idAnalista = chat.getUsuarios().get(i).getNumeroCadastroUsuario();
                        mensagens = mensagemService.findByIdChat(chat);
                        chatResumido.setIdUsuario(idAnalista);
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

    public List<MensagemResumida> resumirMensagem(List<Mensagem> mensagemList) {
        List<MensagemResumida> mensagemResumidaList = new ArrayList<>();
        for (Mensagem mensagem : mensagemList) {
            if (mensagem != null) {
                MensagemResumida mensagemResumida = new MensagemResumida();
                mensagemResumida.setIdMensagem(mensagem.getIdMensagem());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                mensagemResumida.setDataMensagem(sdf.format(mensagem.getDataMensagem()));
                mensagemResumida.setTextoMensagem(mensagem.getTextoMensagem());
                mensagemResumida.setIdChat(mensagem.getIdChat().getIdChat());
                mensagemResumida.setIdUsuario(mensagem.getIdUsuario().getNumeroCadastroUsuario());

                mensagemResumidaList.add(mensagemResumida);
            }
        }
        return mensagemResumidaList;
    }
}
