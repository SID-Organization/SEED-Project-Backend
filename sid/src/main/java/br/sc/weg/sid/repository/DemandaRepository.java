package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {

    List<Demanda> findByTamanhoDemanda(TamanhoDemanda tamanhoDemanda);
    List<Demanda> findBySecaoTIResponsavelDemanda(String secaoTIResponsavelDemanda);
    List<Demanda> findByStatusDemanda(StatusDemanda statusDemanda);
    List<Demanda> findByScoreDemanda(Double scoreDemanda);
    List<Demanda> findBySolicitanteDemanda(Usuario solicitanteDemanda);
    List<Demanda> findByTituloDemanda(String tituloDemanda);

//    @Query(value = "update bus_beneficiadas_demanda set id_demanda = ?1 where ?2")
//    void updateBusBeneficiadasDemanda(Integer idDemanda, Integer idBusBeneficiadasDemanda);

    @Query(value = "select * from demanda order by prazo_elaboracao_demanda ASC;", nativeQuery = true)
    List<Demanda> findByPrazoElaboracaoDemandaAsc();

    @Query(value = "select * from demanda order by prazo_elaboracao_demanda DESC;", nativeQuery = true)
    List<Demanda> findByPrazoElaboracaoDemandaDesc();

    @Query(value = "select * from demanda order by score_demanda ASC;", nativeQuery = true)
    List<Demanda> findByScoreDemandaAsc();

    @Query(value = "select * from demanda order by score_demanda DESC;", nativeQuery = true)
    List<Demanda> findByScoreDemandaDesc();

}
