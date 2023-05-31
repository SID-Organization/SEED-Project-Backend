package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.PdfDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PdfDemandaRepository extends JpaRepository<PdfDemanda, Integer> {

    @Override
    <S extends PdfDemanda> S save(S entity);

    @Override
    Optional<PdfDemanda> findById(Integer integer);

    @Override
    boolean existsById(Integer integer);

    @Override
    void deleteById(Integer integer);

    List<PdfDemanda> findByDemanda(Demanda demanda);
}
