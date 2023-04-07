package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.TabelaCusto;
import br.sc.weg.sid.model.entities.TabelaCustoLinha;
import br.sc.weg.sid.repository.TabelaCustoLinhaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TabelaCustoLinhaService {
    @Autowired
    TabelaCustoLinhaRepository tabelaCustoLinhaRepository;

    public List<TabelaCustoLinha> findAll() {
        return tabelaCustoLinhaRepository.findAll();
    }

    public <S extends TabelaCustoLinha> S save(S entity) {
        return tabelaCustoLinhaRepository.save(entity);
    }

    public Optional<TabelaCustoLinha> findById(Integer integer) {
        return tabelaCustoLinhaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return tabelaCustoLinhaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        tabelaCustoLinhaRepository.deleteById(integer);
    }

    public void deleteByTabelaCusto(TabelaCusto tabelaCusto) {
        tabelaCustoLinhaRepository.deleteByTabelaCusto(tabelaCusto);
    }

    public List<TabelaCustoLinha> findByTabelaCusto(TabelaCusto tabelaCusto) {
        return tabelaCustoLinhaRepository.findByTabelaCusto(tabelaCusto);
    }
}
