package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.LinhaTabela;
import br.sc.weg.sid.model.entities.TabelaCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinhaTabelaRepository extends JpaRepository<LinhaTabela, Integer> {
    @Override
    default <S extends LinhaTabela> S save(S entity) {
        return null;
    }

    @Override
    default Optional<LinhaTabela> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    default boolean existsById(Integer integer) {
        return false;
    }

    @Override
    default void deleteById(Integer integer) {

    }
}
