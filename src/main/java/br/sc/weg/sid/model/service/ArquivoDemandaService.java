package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.ArquivoDemanda;
import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.repository.ArquivoDemandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArquivoDemandaService {

    @Autowired
    ArquivoDemandaRepository arquivoDemandaRepository;

    public List<ArquivoDemanda> findAll() {
        return arquivoDemandaRepository.findAll();
    }

    public <S extends ArquivoDemanda> S save(S entity) {
        return arquivoDemandaRepository.save(entity);
    }

    public Optional<ArquivoDemanda> findById(Integer integer) {
        return arquivoDemandaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return arquivoDemandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        arquivoDemandaRepository.deleteById(integer);
    }
}
