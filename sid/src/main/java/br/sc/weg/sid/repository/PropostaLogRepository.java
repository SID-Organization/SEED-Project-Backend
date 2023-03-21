package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.PropostasLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropostaLogRepository extends JpaRepository<PropostasLog, Integer> {
}
