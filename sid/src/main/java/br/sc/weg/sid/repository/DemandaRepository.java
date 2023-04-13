package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {

    List<Demanda> findByTamanhoDemanda(TamanhoDemanda tamanhoDemanda);
    List<Demanda> findByStatusDemanda(StatusDemanda statusDemanda);
    List<Demanda> findByScoreDemanda(Double scoreDemanda);
    List<Demanda> findBySolicitanteDemanda(Usuario solicitanteDemanda);
    List<Demanda> findByTituloDemanda(String tituloDemanda);
    List<Demanda> findByAnalistaResponsavelDemanda(Usuario analistaResponsavelDemanda);
    List<Demanda> findByGerenteDaAreaDemanda(Usuario gerenteDaAreaDemanda);
    List<Demanda> findByGestorResponsavelDemanda(Usuario gestorResponsavelDemanda);
    List<Demanda> findRascunhosBySolicitanteDemanda(Usuario solicitanteDemanda);
    @Query(value = "select * from demanda order by prazo_elaboracao_demanda ASC;", nativeQuery = true)
    List<Demanda> findByPrazoElaboracaoDemandaAsc();

    @Query(value = "select * from demanda order by prazo_elaboracao_demanda DESC;", nativeQuery = true)
    List<Demanda> findByPrazoElaboracaoDemandaDesc();

    @Query(value = "select * from demanda order by score_demanda ASC;", nativeQuery = true)
    List<Demanda> findByScoreDemandaAsc();

    @Query(value = "select * from demanda order by score_demanda DESC;", nativeQuery = true)
    List<Demanda> findByScoreDemandaDesc();

    @Query(value = "select * from demanda where status_demanda = 'RASCUNHO' and solicitante_demanda = ?1", nativeQuery = true)
    List<Demanda> findRascunhosBySolicitanteDemanda(Integer solicitanteDemanda);

}
