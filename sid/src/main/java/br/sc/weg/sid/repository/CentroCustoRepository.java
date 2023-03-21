package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.CentroCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CentroCustoRepository extends JpaRepository<CentroCusto, Integer> {

    @Override
    <S extends CentroCusto> S save(S entity);

    @Override
    Optional<CentroCusto> findById(Integer integer);

    @Override
    boolean existsById(Integer integer);

    @Override
    void deleteById(Integer integer);
}
