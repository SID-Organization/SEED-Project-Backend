package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.AtaDG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtaDGRepository extends JpaRepository<AtaDG, Integer> {

    List<AtaDG>findByAtaAtaDg(Ata ata);

}
