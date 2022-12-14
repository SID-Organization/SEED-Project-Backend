package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.Mensagem;
import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    public List<Mensagem> findAll() {
        return mensagemRepository.findAll();
    }

    public <S extends Mensagem> S save(S entity) {
        return mensagemRepository.save(entity);
    }

    public Optional<Mensagem> findById(Integer integer) {
        return mensagemRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return mensagemRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        mensagemRepository.deleteById(integer);
    }

    public List<Mensagem> findByIdChatAndIdUsuario(Chat idChat, Usuario idUsuario) {
        return mensagemRepository.findByIdChatAndIdUsuario(idChat, idUsuario);
    }
}
