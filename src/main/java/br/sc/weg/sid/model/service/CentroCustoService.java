package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.CentroCusto;
import br.sc.weg.sid.repository.CentroCustoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CentroCustoService {

    @Autowired
    private CentroCustoRepository centroCustoRepository;

    public <S extends CentroCusto> S save(S entity) {
        return centroCustoRepository.save(entity);
    }

    public Optional<CentroCusto> findById(Integer integer) {
        return centroCustoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return centroCustoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        centroCustoRepository.deleteById(integer);
    }

    public List<CentroCusto> findAll() {
        return centroCustoRepository.findAll();
    }
}
