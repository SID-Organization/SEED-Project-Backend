package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.DecisaoProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisaoPropostaRepository extends JpaRepository<DecisaoProposta, Integer> {

}
