package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.repository.AtaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AtaService {
    @Autowired
    private AtaRepository ataRepository;

    public List<Ata> findAll() {
        return ataRepository.findAll();
    }

    public <S extends Ata> S save(S entity) {
        return ataRepository.save(entity);
    }

    public Optional<Ata> findById(Integer integer) {
        return ataRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return ataRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        ataRepository.deleteById(integer);
    }

    public Ata findByNumeroDgAta(Integer numeroDgAta) {
        return ataRepository.findByNumeroDgAta(numeroDgAta);
    }

//    public List<Ata> findByPautaAta(Pauta pautaAta) {
//        return ataRepository.findByPautaAta(pautaAta);
//    }
}
