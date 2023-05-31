package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.StatusWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoWorkflowRepository extends JpaRepository<HistoricoWorkflow, Integer> {
    List<HistoricoWorkflow> findByDemandaHistorico(Demanda demanda);
    List<HistoricoWorkflow> findByStatusWorkflow(StatusWorkflow statusWorkflow);
    List<HistoricoWorkflow> findByIdResponsavel(Usuario numeroCadastroResponsavel);
}
