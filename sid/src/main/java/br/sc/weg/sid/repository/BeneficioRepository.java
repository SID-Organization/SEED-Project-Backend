package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Beneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Integer> {
}
