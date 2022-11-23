package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.LinhaTabela;
import br.sc.weg.sid.model.entities.TabelaCusto;
import br.sc.weg.sid.repository.LinhaTabelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinhaTabelaService {
    @Autowired
    LinhaTabelaRepository linhaTabelaRepository;

    public List<LinhaTabela> findAllByIdTabelaCusto(TabelaCusto idTabelaCusto) {
        return linhaTabelaRepository.findAllByIdTabelaCusto(idTabelaCusto);
    }

    public List<LinhaTabela> findAll() {
        return linhaTabelaRepository.findAll();
    }

    public <S extends LinhaTabela> S save(S entity) {
        return linhaTabelaRepository.save(entity);
    }

    public Optional<LinhaTabela> findById(Integer integer) {
        return linhaTabelaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return linhaTabelaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        linhaTabelaRepository.deleteById(integer);
    }
}
