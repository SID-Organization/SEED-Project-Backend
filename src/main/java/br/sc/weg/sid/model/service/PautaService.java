package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PautaService {

    @Autowired
    PautaRepository pautaRepository;

    public List<Pauta> findAll() {
        return pautaRepository.findAll();
    }

    public <S extends Pauta> S save(S entity) {
        return pautaRepository.save(entity);
    }

    public Optional<Pauta> findById(Integer integer) {
        return pautaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return pautaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        pautaRepository.deleteById(integer);
    }
}
