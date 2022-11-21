package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.LinhaTabela;
import br.sc.weg.sid.model.entities.TabelaCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinhaTabelaRepository extends JpaRepository<LinhaTabela, Integer> {
    List<LinhaTabela> findAllByIdTabelaCusto(TabelaCusto idTabelaCusto);
}
