package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Integer> {
    List<Proposta> findAllByAprovadoWorkflowProposta(Integer aprovadoWorkflowProposta);

    List<Proposta> findByDemandaProposta(Demanda idDemanda);

    List<Proposta> findAllByPaybackProposta(Double paybackProposta);
}
