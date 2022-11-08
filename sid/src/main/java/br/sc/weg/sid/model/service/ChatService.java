package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    ChatRepository chatRepository;

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public <S extends Chat> S save(S entity) {
        return chatRepository.save(entity);
    }

    public Optional<Chat> findById(Integer integer) {
        return chatRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return chatRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        chatRepository.deleteById(integer);
    }
}
