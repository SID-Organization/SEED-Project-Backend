package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.PropostasLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropostaLogRepository extends JpaRepository<PropostasLog, Integer> {

    Optional<PropostasLog> findByPropostaPropostaLog(Proposta proposta);

}
