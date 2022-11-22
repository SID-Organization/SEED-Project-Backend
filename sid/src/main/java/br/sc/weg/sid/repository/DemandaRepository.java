package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {

    List<Demanda> findByTamanhoDemanda(Tamanho tamanhoDemanda);
    List<Demanda> findBySecaoTIResponsavel(Secao secaoTIResponsavelDemanda);
    List<Demanda> findByStatusDemanda(Status statusDemanda);
    List<Demanda> findByScoreDemanda(Double scoreDemanda);
    List<Demanda> findBySolicitanteDemanda(Usuario solicitanteDemanda);
    List<Demanda> findByTituloDemanda(String tituloDemanda);

    @Query(value = "select * from demanda order by prazo_elaboracao_demanda ASC;", nativeQuery = true)
    List<Demanda> findByPrazoElaboracaoDemandaAsc();

    @Query(value = "select * from demanda order by prazo_elaboracao_demanda DESC;", nativeQuery = true)
    List<Demanda> findByPrazoElaboracaoDemandaDesc();

    @Query(value = "select * from demanda order by score_demanda ASC;", nativeQuery = true)
    List<Demanda> findByScoreDemandaAsc();

    @Query(value = "select * from demanda order by score_demanda DESC;", nativeQuery = true)
    List<Demanda> findByScoreDemandaDesc();

}
