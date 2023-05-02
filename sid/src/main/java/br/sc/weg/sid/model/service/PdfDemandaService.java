package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.PdfDemanda;
import br.sc.weg.sid.repository.PdfDemandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PdfDemandaService {

    @Autowired
    PdfDemandaRepository pdfDemandaRepository;

    public <S extends PdfDemanda> S save(S entity) {
        return pdfDemandaRepository.save(entity);
    }

    public Optional<PdfDemanda> findById(Integer integer) {
        return pdfDemandaRepository.findById(integer);
    }

    public List<PdfDemanda> findByDemanda(Demanda demanda) {
        return pdfDemandaRepository.findByDemanda(demanda);
    }

    public boolean existsById(Integer integer) {
        return pdfDemandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        pdfDemandaRepository.deleteById(integer);
    }

    public void deleteAll(List<PdfDemanda> pdfDemanda) {
        pdfDemandaRepository.deleteAll(pdfDemanda);
    }
}
