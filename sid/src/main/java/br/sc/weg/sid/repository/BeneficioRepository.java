package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Beneficio;
import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.enums.TipoBeneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Integer> {
    Optional<Beneficio> findByDemandaBeneficioAndTipoBeneficio(Demanda demandaBeneficio, TipoBeneficio tipoBeneficio);
}
