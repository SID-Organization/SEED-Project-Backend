package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Notificacao;
import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacaoService {
    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public List<Notificacao> findAll() {
        return notificacaoRepository.findAll();
    }

    public <S extends Notificacao> S save(S entity) {
        return notificacaoRepository.save(entity);
    }

    public Optional<Notificacao> findById(Integer integer) {
        return notificacaoRepository.findById(integer);
    }

    public List<Notificacao> findByUsuario(Usuario usuario) {
        return notificacaoRepository.findByUsuario(usuario);
    }

    public boolean existsById(Integer integer) {
        return notificacaoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        notificacaoRepository.deleteById(integer);
    }
}
