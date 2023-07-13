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

//    CLASSIFICADO_PELO_ANALISTA("Classificado pelo analista"),
//    ABERTA("Aberta"),
//    EM_EDICAO("Em edição"),
//    APROVADO_PELO_GERENTE_DA_AREA("Aprovado pelo gerente da área"),
//    PROPOSTA_EM_ELABORACAO("Proposta em elaboração"),
//    PROPOSTA_PRONTA("Proposta pronta"),
//    EM_PAUTA("Em pauta"),
//    APROVADA_EM_COMISSAO("Aprovada em comissão"),
//    APROVADA_EM_DG("Aprovada em DG"),
//    PROPOSTA_EM_EXECUCAO("Proposta em execução"),
//    BUSINESS_CASE("Business Case"),
//    PROPOSTA_EM_SUPORTE("Proposta em suporte"),
//    PROPOSTA_FINALIZADA("Proposta finalizada"),
//    CANCELADA("Cancelada"),
//    RASCUNHO("Rascunho");

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'APROVADA_EM_DG'")
    List<Demanda> findDemandasAprovadasEmDG();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'CANCELADA'")
    List<Demanda> findDemandasCanceladas();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'APROVADA_EM_COMISSAO'")
    List<Demanda> findDemandasAprovadasEmComissao();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'APROVADO_PELO_GERENTE_DA_AREA'")
    List<Demanda> findDemandasAprovadasPeloGerenteDaArea();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'EM_EDICAO'")
    List<Demanda> findDemandasEmEdicao();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'EM_PAUTA'")
    List<Demanda> findDemandasEmPauta();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'PROPOSTA_EM_ELABORACAO'")
    List<Demanda> findDemandasPropostaEmElaboracao();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'PROPOSTA_EM_EXECUCAO'")
    List<Demanda> findDemandasPropostaEmExecucao();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'PROPOSTA_EM_SUPORTE'")
    List<Demanda> findDemandasPropostaEmSuporte();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'PROPOSTA_FINALIZADA'")
    List<Demanda> findDemandasPropostaFinalizada();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'RASCUNHO'")
    List<Demanda> findDemandasRascunho();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'CLASSIFICADO_PELO_ANALISTA'")
    List<Demanda> findDemandasClassificadasPeloAnalista();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'ABERTA'")
    List<Demanda> findDemandasAbertas();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'BUSINESS_CASE'")
    List<Demanda> findDemandasBusinessCase();

    @Query("SELECT d FROM Demanda d WHERE d.statusDemanda = 'PROPOSTA_PRONTA'")
    List<Demanda> findDemandasPropostaPronta();


    List<Demanda> findAllByStatusDemandaIsAndSolicitanteDemandaNumeroCadastroUsuarioIs(StatusDemanda statusDemanda, Integer solicitanteDemanda_numeroCadastroUsuario);
}

