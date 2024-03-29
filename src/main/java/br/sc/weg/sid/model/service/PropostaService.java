package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.repository.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropostaService {

    @Autowired
    PropostaRepository propostaRepository;

    public List<Proposta> findAll() {
        return propostaRepository.findAll();
    }

    public <S extends Proposta> S save(S entity) {
        return propostaRepository.save(entity);
    }

    public Optional<Proposta> findById(Integer integer) {
        return propostaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return propostaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        propostaRepository.deleteById(integer);
    }

    public List<Proposta> findAllByAprovadoWorkflowProposta(Integer aprovadoWorkflowProposta) {
        return propostaRepository.findAllByAprovadoWorkflowProposta(aprovadoWorkflowProposta);
    }

    public List<Proposta> findByDemandaProposta(Demanda idDemanda) {
        return propostaRepository.findByDemandaProposta(idDemanda);
    }

    public List<Proposta> findAllByPaybackProposta(Double paybackProposta) {
        return propostaRepository.findAllByPaybackProposta(paybackProposta);
    }

    public List<Proposta> findAllPropostasEmElaboracaoByAnalista(Integer idAnalista) {
        return propostaRepository.findAllPropostasEmElaboracaoByAnalista(idAnalista);
    }
    public List<Proposta> findAllByForumPauta(Forum forumPauta) {
        return propostaRepository.findAllByForumPauta(forumPauta);
    }

    public List<Proposta> findPropostasAprovadasByForum(Forum forumPauta) {
        return propostaRepository.findPropostasAprovadasByForum(forumPauta);
    }

    public List<Proposta> findPropostasReprovadasByForum(Forum forumPauta) {
        return propostaRepository.findPropostasReprovadasByForum(forumPauta);
    }

}
