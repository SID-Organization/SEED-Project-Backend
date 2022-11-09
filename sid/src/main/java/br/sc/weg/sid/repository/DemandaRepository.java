package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {

    Optional<Demanda> findByTamanhoDemanda(Tamanho tamanhoDemanda);
    Optional<Demanda> findBySecaoTIResponsavel(Secao secaoTIResponsavelDemanda);
    Optional<Demanda> findByStatusDemanda(Status statusDemanda);
    Optional<Demanda> findByScoreDemanda(Integer scoreDemanda);
    Optional<Demanda> findBySolicitanteDemanda(Usuario solicitanteDemanda);

}
