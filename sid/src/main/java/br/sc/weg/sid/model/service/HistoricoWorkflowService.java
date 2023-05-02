package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.StatusWorkflow;
import br.sc.weg.sid.repository.HistoricoWorkflowRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoricoWorkflowService {

    @Autowired
    private HistoricoWorkflowRepository historicoWorkflowRepository;

    public void teste() {
        HistoricoWorkflow historicoWorkflow = historicoWorkflowRepository.findById(1).get();
        HistoricoWorkflow historicoWorkflow2 = new HistoricoWorkflow();

        BeanUtils.copyProperties(historicoWorkflow, historicoWorkflow2);

//        historicoWorkflow2.setVersaoHistorico(2.0);

        System.out.println(historicoWorkflow.equals(historicoWorkflow2));


    }

    public List<HistoricoWorkflow> findAll() {
        return historicoWorkflowRepository.findAll();
    }

    public <S extends HistoricoWorkflow> S save(S entity) {
        return historicoWorkflowRepository.save(entity);
    }

    public Optional<HistoricoWorkflow> findById(Integer integer) {
        return historicoWorkflowRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return historicoWorkflowRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        historicoWorkflowRepository.deleteById(integer);
    }

    public List<HistoricoWorkflow> findByDemandaHistorico(Demanda demandaHistorico) {
        return historicoWorkflowRepository.findByDemandaHistorico(demandaHistorico);
    }

    public List<HistoricoWorkflow> findByStatusWorkflow(StatusWorkflow statusWorkflow){
        return historicoWorkflowRepository.findByStatusWorkflow(statusWorkflow);
    }

    public List<HistoricoWorkflow> findByIdResponsavel(Usuario numeroCadastroResponsavel){
        return historicoWorkflowRepository.findByIdResponsavel(numeroCadastroResponsavel);
    }

}
