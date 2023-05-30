package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.MotivoRecusa;
import br.sc.weg.sid.model.enums.StatusDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivoRecusaRepository extends JpaRepository<MotivoRecusa, Integer> {

    MotivoRecusa findByIdHistoricoWorkflowAndStatusDemandaMotivoRecusa(Integer idHistoricoWorkflow, StatusDemanda statusDemandaMotivoRecusa);

}
