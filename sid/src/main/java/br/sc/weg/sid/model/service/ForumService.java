package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumService {
    @Autowired
    private ForumRepository forumRepository;

    public List<Forum> findAll() {
        return forumRepository.findAll();
    }

    public <S extends Forum> S save(S entity) {
        return forumRepository.save(entity);
    }

    public Optional<Forum> findById(Integer integer) {
        return forumRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return forumRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        forumRepository.deleteById(integer);
    }
}
