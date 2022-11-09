package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.ResponsaveisNegocio;
import br.sc.weg.sid.repository.ResponsaveisNegocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponsaveisNegocioService {

    @Autowired
    ResponsaveisNegocioRepository responsaveisNegocioRepository;

    public List<ResponsaveisNegocio> findAll() {
        return responsaveisNegocioRepository.findAll();
    }

    public <S extends ResponsaveisNegocio> S save(S entity) {
        return responsaveisNegocioRepository.save(entity);
    }

    public Optional<ResponsaveisNegocio> findById(Integer integer) {
        return responsaveisNegocioRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return responsaveisNegocioRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        responsaveisNegocioRepository.deleteById(integer);
    }
}
