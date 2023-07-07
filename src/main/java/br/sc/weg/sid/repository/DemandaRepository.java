package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.enums.StatusDemanda;
import br.sc.weg.sid.model.enums.TamanhoDemanda;
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

    @Query(value = "select * from demanda join analista_responsavel_demanda analista on analista.numero_cadastro_usuario = ?1 and" +
            " demanda.id_demanda = analista.id_demanda", nativeQuery = true)
    List<Demanda> findByAnalistasResponsaveisDemanda(Integer numeroCadastroAnalista);
    List<Demanda> findByGerenteDaAreaDemanda(Usuario gerenteDaAreaDemanda);
    List<Demanda> findByGestorResponsavelDemanda(Usuario gestorResponsavelDemanda);
    List<Demanda> findByStatusDemandaAndSolicitanteDemanda(StatusDemanda statusDemanda, Usuario solicitanteDemanda);

    List<Demanda> findByStatusDemandaAndAnalistasResponsaveisDemandaIsNull(StatusDemanda statusDemanda);

    List<Demanda> findAllByOrderByPrazoElaboracaoDemandaAsc();
    List<Demanda> findAllByOrderByPrazoElaboracaoDemandaDesc();

    List<Demanda> findAllByOrderByScoreDemandaAsc();

    @Query("SELECT d FROM Demanda d WHERE d IN :demandas ORDER BY d.scoreDemanda DESC")
    List<Demanda> orderByScoreDemandaDesc(List<Demanda> demandas);

    List<Demanda> findAllByStatusDemandaIsAndSolicitanteDemandaNumeroCadastroUsuarioIs(StatusDemanda statusDemanda, Integer solicitanteDemanda_numeroCadastroUsuario);
}

