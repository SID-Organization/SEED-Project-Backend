package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.DecisaoProposta;
import br.sc.weg.sid.repository.DecisaoPropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DecisaoPropostaService {
    @Autowired
    private DecisaoPropostaRepository decisaoPropostaRepository;

    public List<DecisaoProposta> findAll() {
        return decisaoPropostaRepository.findAll();
    }

    public <S extends DecisaoProposta> S save(S entity) {
        return decisaoPropostaRepository.save(entity);
    }

    public Optional<DecisaoProposta> findById(Integer integer) {
        return decisaoPropostaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return decisaoPropostaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        decisaoPropostaRepository.deleteById(integer);
    }
}
