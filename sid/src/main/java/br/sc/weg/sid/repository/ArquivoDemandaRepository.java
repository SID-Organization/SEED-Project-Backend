package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.ArquivoDemanda;
import br.sc.weg.sid.model.entities.Demanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoDemandaRepository extends JpaRepository<ArquivoDemanda, Integer> {

}
