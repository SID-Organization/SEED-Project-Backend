package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.PdfProposta;
import br.sc.weg.sid.repository.PdfPropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PdfPropostaService {

    @Autowired
    private PdfPropostaRepository pdfPropostaRepository;

    public <S extends PdfProposta> S save(S entity) {
        return pdfPropostaRepository.save(entity);
    }

    public Optional<PdfProposta> findById(Integer integer) {
        return pdfPropostaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return pdfPropostaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        pdfPropostaRepository.deleteById(integer);
    }
}
