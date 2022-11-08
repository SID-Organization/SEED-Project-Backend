package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.repository.DemandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemandaService {

    @Autowired
    DemandaRepository demandaRepository;

    public List<Demanda> findAll() {
        return demandaRepository.findAll();
    }

    public <S extends Demanda> S save(S entity) {
        return demandaRepository.save(entity);
    }

    public Optional<Demanda> findById(Integer integer) {
        return demandaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return demandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        demandaRepository.deleteById(integer);
    }
}
