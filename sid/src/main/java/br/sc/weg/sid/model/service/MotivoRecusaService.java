package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.MotivoRecusa;
import br.sc.weg.sid.repository.MotivoRecusaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotivoRecusaService {

    @Autowired
    MotivoRecusaRepository motivoRecusaRepository;

    public List<MotivoRecusa> findAll() {
        return motivoRecusaRepository.findAll();
    }

    public <S extends MotivoRecusa> S save(S entity) {
        return motivoRecusaRepository.save(entity);
    }

    public Optional<MotivoRecusa> findById(Integer integer) {
        return motivoRecusaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return motivoRecusaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        motivoRecusaRepository.deleteById(integer);
    }
}
