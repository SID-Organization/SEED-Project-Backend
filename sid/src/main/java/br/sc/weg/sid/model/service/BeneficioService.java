package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Beneficio;
import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.repository.BeneficioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficioService {

    @Autowired
    BeneficioRepository beneficioRepository;

    public List<Beneficio> findAll() {
        return beneficioRepository.findAll();
    }

    public <S extends Beneficio> S save(S entity) {
        return beneficioRepository.save(entity);
    }

    public Optional<Beneficio> findById(Integer integer) {
        return beneficioRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return beneficioRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        beneficioRepository.deleteById(integer);
    }

    public List<Beneficio> findByDemandaBeneficio(Demanda demandaBeneficio) {
        return beneficioRepository.findByDemandaBeneficio(demandaBeneficio);
    }

}
