package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.PropostasLog;
import br.sc.weg.sid.repository.PropostaLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropostaLogService {

    @Autowired
    PropostaLogRepository propostaLogRepository;

    public Optional<PropostasLog> findById(Integer integer) {
        return propostaLogRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return propostaLogRepository.existsById(integer);
    }

    public void deleteById(Integer id) {
        propostaLogRepository.deleteById(id);
    }

    public List<PropostasLog> findAll() {
        return propostaLogRepository.findAll();
    }

    public <S extends PropostasLog> S save(S entity) {
        return propostaLogRepository.save(entity);
    }

    public void deleteAll() {
        propostaLogRepository.deleteAll();
    }
}
