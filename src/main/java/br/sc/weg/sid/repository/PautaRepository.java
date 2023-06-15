package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Integer> {

}
