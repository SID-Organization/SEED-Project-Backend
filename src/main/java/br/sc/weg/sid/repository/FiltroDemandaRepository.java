package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.FiltroDemanda;
import br.sc.weg.sid.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiltroDemandaRepository extends JpaRepository<FiltroDemanda, Integer> {

     List<FiltroDemanda> findAllByUsuario(Usuario usuario);

}
