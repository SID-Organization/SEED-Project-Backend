package br.sc.weg.sid.repository;


import br.sc.weg.sid.model.entities.CentroCustoTabelaCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroCustoTabelaCustoRepository extends JpaRepository<CentroCustoTabelaCusto, Integer> {
}
