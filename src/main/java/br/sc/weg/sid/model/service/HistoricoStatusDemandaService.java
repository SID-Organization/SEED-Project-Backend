package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.HistoricoStatusDemanda;
import br.sc.weg.sid.repository.HistoricoStatusDemandaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HistoricoStatusDemandaService {
    @Autowired
    private HistoricoStatusDemandaRepository historicoStatusDemandaRepository;

    public List<HistoricoStatusDemanda> findAll() {
        return historicoStatusDemandaRepository.findAll();
    }

    public <S extends HistoricoStatusDemanda> S save(S entity) {
        return historicoStatusDemandaRepository.save(entity);
    }

    public Optional<HistoricoStatusDemanda> findById(Integer integer) {
        return historicoStatusDemandaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return historicoStatusDemandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        historicoStatusDemandaRepository.deleteById(integer);
    }
}
