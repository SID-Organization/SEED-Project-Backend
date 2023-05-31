package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.BusinessUnity;
import br.sc.weg.sid.repository.BusinessUnityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessUnityService {

    @Autowired
    private BusinessUnityRepository repository;

    public List<BusinessUnity> findAll() {
        return repository.findAll();
    }

    public <S extends BusinessUnity> S save(S entity) {
        return repository.save(entity);
    }

    public Optional<BusinessUnity> findById(Integer integer) {
        return repository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return repository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        repository.deleteById(integer);
    }

    public Optional<BusinessUnity> findByNomeBusinessUnity(String nomeBusinessUnity) {
        return repository.findByNomeBusinessUnity(nomeBusinessUnity);
    }

}
