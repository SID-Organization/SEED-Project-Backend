package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.AtaDG;
import br.sc.weg.sid.repository.AtaDGRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AtaDGService {

    @Autowired
    AtaDGRepository ataDGRepository;

    public List<AtaDG> findAll() {
        return ataDGRepository.findAll();
    }

    public <S extends AtaDG> S save(S entity) {
        return ataDGRepository.save(entity);
    }

    public Optional<AtaDG> findById(Integer integer) {
        return ataDGRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return ataDGRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        ataDGRepository.deleteById(integer);
    }
}
