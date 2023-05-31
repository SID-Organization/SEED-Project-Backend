package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Comissao;
import br.sc.weg.sid.repository.ComissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComissaoService {

    @Autowired
    ComissaoRepository comissaoRepository;

    public List<Comissao> findAll() {
        return comissaoRepository.findAll();
    }

    public <S extends Comissao> S save(S entity) {
        return comissaoRepository.save(entity);
    }

    public Optional<Comissao> findById(Integer integer) {
        return comissaoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return comissaoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        comissaoRepository.deleteById(integer);
    }
}
