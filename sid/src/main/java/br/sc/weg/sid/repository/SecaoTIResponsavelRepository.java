package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.SecaoTIResponsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecaoTIResponsavelRepository extends JpaRepository<SecaoTIResponsavel, Integer> {

}
