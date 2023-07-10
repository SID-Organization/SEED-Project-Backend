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
