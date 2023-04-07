package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.TabelaCusto;
import br.sc.weg.sid.repository.TabelaCustoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TabelaCustoService {

    @Autowired
    TabelaCustoRepository tabelaCustoRepository;

    public List<TabelaCusto> findAll() {
        return tabelaCustoRepository.findAll();
    }

    public <S extends TabelaCusto> S save(S entity) {
        return tabelaCustoRepository.save(entity);
    }

    public Optional<TabelaCusto> findById(Integer integer) {
        return tabelaCustoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return tabelaCustoRepository.existsById(integer);
    }

    public List<TabelaCusto> findByPropostaTabelaCusto(Proposta propostaTabelaCusto) {
        return tabelaCustoRepository.findByPropostaTabelaCusto(propostaTabelaCusto);

    }

    public void deleteById(Integer integer) {
        tabelaCustoRepository.deleteById(integer);
    }

    public void deleteAll(List<TabelaCusto> tabelaCustoList) {
        tabelaCustoRepository.deleteAll(tabelaCustoList);
    }
}
