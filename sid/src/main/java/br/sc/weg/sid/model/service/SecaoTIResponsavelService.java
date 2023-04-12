package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.SecaoTIResponsavel;
import br.sc.weg.sid.repository.SecaoTIResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecaoTIResponsavelService {

    @Autowired
    SecaoTIResponsavelRepository secaoTIResponsavelRepository;

    public <S extends SecaoTIResponsavel> S save(S entity) {
        return secaoTIResponsavelRepository.save(entity);
    }

    public Optional<SecaoTIResponsavel> findById(Integer integer) {
        return secaoTIResponsavelRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return secaoTIResponsavelRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        secaoTIResponsavelRepository.deleteById(integer);
    }

    public List<SecaoTIResponsavel> findAll() {
        return secaoTIResponsavelRepository.findAll();
    }
}
