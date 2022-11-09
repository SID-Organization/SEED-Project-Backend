package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.BusBeneficiadas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusBeneficiadasRepository extends JpaRepository<BusBeneficiadas, Integer> {

}
