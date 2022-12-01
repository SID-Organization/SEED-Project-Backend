package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.repository.DemandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemandaService {

    @Autowired
    DemandaRepository demandaRepository;

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

    public List<Demanda> findByTamanhoDemanda(Tamanho tamanhoDemanda) {
        return demandaRepository.findByTamanhoDemanda(tamanhoDemanda);
    }

    public List<Demanda> findBySecaoTIResponsavel(Secao secaoTIResponsavelDemanda) {
        return demandaRepository.findBySecaoTIResponsavel(secaoTIResponsavelDemanda);
    }

    public List<Demanda> findByStatusDemanda(Status statusDemanda) {
        return demandaRepository.findByStatusDemanda(statusDemanda);
    }

    public List<Demanda> findByScoreDemanda(Double scoreDemanda) {
        return demandaRepository.findByScoreDemanda(scoreDemanda);
    }

    public List<Demanda> findBySolicitanteDemanda(Usuario solicitanteDemanda) {
        return demandaRepository.findBySolicitanteDemanda(solicitanteDemanda);
    }

    public List<Demanda> findByPrazoElaboracaoDemandaAsc() {
        return demandaRepository.findByPrazoElaboracaoDemandaAsc();
    }

    public List<Demanda> findByPrazoElaboracaoDemandaDesc() {
        return demandaRepository.findByPrazoElaboracaoDemandaDesc();
    }

    public List<Demanda> findByTituloDemanda(String tituloDemanda) {
        return demandaRepository.findByTituloDemanda(tituloDemanda);
    }



}
