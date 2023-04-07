package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.TabelaCusto;
import br.sc.weg.sid.model.entities.TabelaCustoLinha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TabelaCustoLinhaRepository extends JpaRepository<TabelaCustoLinha, Integer> {
    public void deleteByTabelaCusto(TabelaCusto tabelaCusto);

    public List<TabelaCustoLinha> findByTabelaCusto(TabelaCusto tabelaCusto);
}
