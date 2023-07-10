package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Integer> {
    List<Proposta> findAllByAprovadoWorkflowProposta(Integer aprovadoWorkflowProposta);

    List<Proposta> findByDemandaProposta(Demanda idDemanda);

    List<Proposta> findAllByPaybackProposta(Double paybackProposta);

    //SELECT proposta.* FROM proposta INNER JOIN demanda ON proposta.demanda_proposta = demanda.id_demanda INNER JOIN analista_responsavel_demanda ard ON demanda.id_demanda = ard.id_demanda WHERE demanda.status_demanda = 'PROPOSTA_EM_ELABORACAO';
    @Query(value = "SELECT proposta.* FROM proposta INNER JOIN demanda ON proposta.demanda_proposta = demanda.id_demanda INNER JOIN analista_responsavel_demanda ard ON demanda.id_demanda = ard.id_demanda AND ard.numero_cadastro_usuario = ?1 WHERE demanda.status_demanda = 'PROPOSTA_EM_ELABORACAO'", nativeQuery = true)
    List<Proposta> findAllPropostasEmElaboracaoByAnalista(Integer idAnalista);

    List<Proposta> findAllByForumPauta(Forum forumPauta);

    // Métodos personalizados para obter a quantidade de demandas aprovadas ou reprovadas em um fórum em um determinado mês
    @Query("SELECT p FROM Proposta p " +
            "JOIN p.demandaProposta d " +
            "JOIN p.forumPauta fp " +
            "WHERE fp = :forumPauta " +
            "AND d.statusDemanda = 'APROVADA_EM_DG'")
    List<Proposta> findPropostasAprovadasByForum(Forum forumPauta);


    @Query("SELECT p FROM Proposta p " +
            "JOIN p.demandaProposta d " +
            "JOIN p.forumPauta fp " +
            "WHERE fp = :forumPauta " +
            "AND d.statusDemanda = 'CANCELADA' ")
    List<Proposta> findPropostasReprovadasByForum(Forum forumPauta);


}
