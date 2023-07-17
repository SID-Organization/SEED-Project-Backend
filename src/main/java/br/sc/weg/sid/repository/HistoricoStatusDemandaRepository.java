package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.HistoricoStatusDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoStatusDemandaRepository extends JpaRepository<HistoricoStatusDemanda, Integer> {

    List<HistoricoStatusDemanda> findByDemanda(Demanda demanda);
}
