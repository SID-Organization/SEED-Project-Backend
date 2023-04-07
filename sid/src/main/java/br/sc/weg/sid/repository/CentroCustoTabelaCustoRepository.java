package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.CentroCustoTabelaCusto;
import br.sc.weg.sid.model.entities.TabelaCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentroCustoTabelaCustoRepository extends JpaRepository<CentroCustoTabelaCusto, Integer> {
    public void deleteByTabelaCusto(TabelaCusto tabelaCusto);

    public List<CentroCustoTabelaCusto> findByTabelaCusto(TabelaCusto tabelaCusto);
}
