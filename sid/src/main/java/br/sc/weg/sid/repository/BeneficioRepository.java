package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Beneficio;
import br.sc.weg.sid.model.entities.Demanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Integer> {
    List<Beneficio> findByDemandaBeneficio(Demanda demandaBeneficio);
}
