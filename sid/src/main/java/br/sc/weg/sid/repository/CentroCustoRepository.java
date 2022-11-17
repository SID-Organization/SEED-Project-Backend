package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.CentroCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CentroCustoRepository extends JpaRepository<CentroCusto, Integer> {
    Optional<CentroCusto> findByNomeCentroCusto(String nomeCentroCusto);
}
