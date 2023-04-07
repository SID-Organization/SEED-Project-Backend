package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.CentroCustoTabelaCusto;
import br.sc.weg.sid.model.entities.TabelaCusto;
import br.sc.weg.sid.repository.CentroCustoTabelaCustoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CentroCustoTabelaCustoService {

    @Autowired
    CentroCustoTabelaCustoRepository centroCustoTabelaCustoRepository;

    public List<CentroCustoTabelaCusto> findAll() {
        return centroCustoTabelaCustoRepository.findAll();
    }

    public <S extends CentroCustoTabelaCusto> S save(S entity) {
        return centroCustoTabelaCustoRepository.save(entity);
    }

    public Optional<CentroCustoTabelaCusto> findById(Integer integer) {
        return centroCustoTabelaCustoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return centroCustoTabelaCustoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        centroCustoTabelaCustoRepository.deleteById(integer);
    }

    public List<CentroCustoTabelaCusto> findByTabelaCusto(TabelaCusto tabelaCusto) {
        return centroCustoTabelaCustoRepository.findByTabelaCusto(tabelaCusto);
    }

    public void deleteAll(List<CentroCustoTabelaCusto> centroCustoTabelaCustoList) {
        centroCustoTabelaCustoRepository.deleteAll(centroCustoTabelaCustoList);
    }
}
