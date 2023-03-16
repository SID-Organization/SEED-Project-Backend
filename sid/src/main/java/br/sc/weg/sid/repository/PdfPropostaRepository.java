package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.PdfProposta;
import br.sc.weg.sid.model.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PdfPropostaRepository extends JpaRepository<PdfProposta, Integer> {

    @Override
    <S extends PdfProposta> S save(S entity);

    @Override
    Optional<PdfProposta> findById(Integer integer);

    Optional<PdfProposta> findByProposta(Proposta proposta);

    @Override
    boolean existsById(Integer integer);

    @Override
    void deleteById(Integer integer);
}
