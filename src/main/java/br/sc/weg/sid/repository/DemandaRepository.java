package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.enums.StatusDemanda;
import br.sc.weg.sid.model.enums.TamanhoDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
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
    List<Demanda> findByStatusDemandaAndSolicitanteDemanda(StatusDemanda statusDemanda, Usuario solicitanteDemanda);

    List<Demanda> findByStatusDemandaAndAnalistaResponsavelDemandaIsNull(StatusDemanda statusDemanda);

    List<Demanda> findAllByOrderByPrazoElaboracaoDemandaAsc();
    List<Demanda> findAllByOrderByPrazoElaboracaoDemandaDesc();

    List<Demanda> findAllByOrderByScoreDemandaAsc();
    List<Demanda> findAllByOrderByScoreDemandaDesc();

    List<Demanda> findAllByStatusDemandaIsAndSolicitanteDemandaNumeroCadastroUsuarioIs(StatusDemanda statusDemanda, Integer solicitanteDemanda_numeroCadastroUsuario);
}

