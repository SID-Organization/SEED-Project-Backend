package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.FiltroDemanda;
import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.repository.FiltroDemandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FiltroDemandaService {

    @Autowired
    private FiltroDemandaRepository filtroDemandaRepository;

    public List<FiltroDemanda> findAll() {
        return filtroDemandaRepository.findAll();
    }

    public <S extends FiltroDemanda> S save(S entity) {
        return filtroDemandaRepository.save(entity);
    }

    public Optional<FiltroDemanda> findById(Integer integer) {
        return filtroDemandaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return filtroDemandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        filtroDemandaRepository.deleteById(integer);
    }

    public List<FiltroDemanda> findAllByUsuario(Usuario usuario) {
        return filtroDemandaRepository.findAllByUsuario(usuario);
    }
}
