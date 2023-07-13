package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.StatusDemanda;
import br.sc.weg.sid.model.enums.TamanhoDemanda;
import br.sc.weg.sid.repository.DemandaRepository;
import br.sc.weg.sid.utils.DemandaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemandaService {

    @Autowired
    DemandaRepository demandaRepository;

    @Autowired
    DemandaUtil demandaUtil;

    public List<Demanda> findAll() {
        return demandaRepository.findAll();
    }

    public <S extends Demanda> S save(S entity) {
        return demandaRepository.save(entity);
    }

    public Optional<Demanda> findById(Integer integer) {
        return demandaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return demandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        demandaRepository.deleteById(integer);
    }

    public List<Demanda> findByTamanhoDemanda(TamanhoDemanda tamanhoDemanda) {
        return demandaRepository.findByTamanhoDemanda(tamanhoDemanda);
    }

    public List<Demanda> findByStatusDemanda(StatusDemanda statusDemanda) {
        return demandaRepository.findByStatusDemanda(statusDemanda);
    }

    public List<Demanda> findByStatusDemandaAndAnalistasResponsaveisDemandaIsNull(StatusDemanda statusDemanda) {
        return demandaRepository.findByStatusDemandaAndAnalistasResponsaveisDemandaIsNull(statusDemanda);
    }

    public List<Demanda> findByScoreDemanda(Double scoreDemanda) {
        return demandaRepository.findByScoreDemanda(scoreDemanda);
    }

    public List<Demanda> orderByScoreDemandaDesc(List<Demanda> demandas) {
        return demandaRepository.orderByScoreDemandaDesc(demandas);
    }

    public List<Demanda> findAllByOrderByScoreDemandaAsc() {
        return demandaRepository.findAllByOrderByScoreDemandaAsc();
    }

    public List<Demanda> findBySolicitanteDemanda(Usuario solicitanteDemanda) {
        return demandaRepository.findBySolicitanteDemanda(solicitanteDemanda);
    }

    public List<Demanda> findByAnalistasResponsaveisDemanda(Integer numeroCadastroAnalista) {
        return demandaRepository.findByAnalistasResponsaveisDemanda(numeroCadastroAnalista);
    }

    public List<Demanda> findByGerenteDaAreaDemanda(Usuario gerenteDaAreaDemanda) {
        return demandaRepository.findByGerenteDaAreaDemanda(gerenteDaAreaDemanda);
    }

    public List<Demanda> findByGestorResponsavelDemanda(Usuario gestorResponsavelDemanda) {
        return demandaRepository.findByGestorResponsavelDemanda(gestorResponsavelDemanda);
    }

    public List<Demanda> findByPrazoElaboracaoDemandaAsc() {
        return demandaRepository.findAllByOrderByPrazoElaboracaoDemandaAsc();
    }

    public List<Demanda> findByPrazoElaboracaoDemandaDesc() {
        return demandaRepository.findAllByOrderByPrazoElaboracaoDemandaDesc();
    }

    public List<Demanda> findByTituloDemanda(String tituloDemanda) {
        return demandaRepository.findByTituloDemanda(tituloDemanda);
    }

    public List<Demanda> findRascunhosBySolicitanteDemanda(Integer solicitanteDemanda) {
        return demandaRepository
                .findAllByStatusDemandaIsAndSolicitanteDemandaNumeroCadastroUsuarioIs(
                        StatusDemanda.RASCUNHO, solicitanteDemanda);
    }

    public List<Demanda> findByStatusDemandaAndSolicitanteDemanda(StatusDemanda statusDemanda, Usuario solicitanteDemanda) {
        return demandaRepository.findByStatusDemandaAndSolicitanteDemanda(statusDemanda, solicitanteDemanda);
    }

    public List<Demanda> findDemandasAprovadasEmDG() {
        return demandaRepository.findDemandasAprovadasEmDG();
    }

    public List<Demanda> findDemandasCanceladas() {
        return demandaRepository.findDemandasCanceladas();
    }

    public List<Demanda> findDemandasAprovadasEmComissao() {
        return demandaRepository.findDemandasAprovadasEmComissao();
    }

    public List<Demanda> findDemandasAprovadasPeloGerenteDaArea() {
        return demandaRepository.findDemandasAprovadasPeloGerenteDaArea();
    }

    public List<Demanda> findDemandasEmEdicao() {
        return demandaRepository.findDemandasEmEdicao();
    }

    public List<Demanda> findDemandasEmPauta() {
        return demandaRepository.findDemandasEmPauta();
    }

    public List<Demanda> findDemandasPropostaEmElaboracao() {
        return demandaRepository.findDemandasPropostaEmElaboracao();
    }

    public List<Demanda> findDemandasPropostaEmExecucao() {
        return demandaRepository.findDemandasPropostaEmExecucao();
    }

    public List<Demanda> findDemandasPropostaEmSuporte() {
        return demandaRepository.findDemandasPropostaEmSuporte();
    }

    public List<Demanda> findDemandasPropostaFinalizada() {
        return demandaRepository.findDemandasPropostaFinalizada();
    }

    public List<Demanda> findDemandasRascunho() {
        return demandaRepository.findDemandasRascunho();
    }

    public List<Demanda> findDemandasClassificadasPeloAnalista() {
        return demandaRepository.findDemandasClassificadasPeloAnalista();
    }

    public List<Demanda> findDemandasAbertas() {
        return demandaRepository.findDemandasAbertas();
    }

    public List<Demanda> findDemandasBusinessCase() {
        return demandaRepository.findDemandasBusinessCase();
    }

    public List<Demanda> findDemandasPropostaPronta() {
        return demandaRepository.findDemandasPropostaPronta();
    }

    //    public void updateBusBeneficiadasDemanda(Integer idDemanda, Integer idBusBeneficiadasDemanda) {
//        demandaRepository.updateBusBeneficiadasDemanda(idDemanda, idBusBeneficiadasDemanda);
//    }

}
