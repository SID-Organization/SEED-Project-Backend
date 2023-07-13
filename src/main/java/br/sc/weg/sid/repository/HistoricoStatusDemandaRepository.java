package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.HistoricoStatusDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoStatusDemandaRepository extends JpaRepository<HistoricoStatusDemanda, Integer> {
}
