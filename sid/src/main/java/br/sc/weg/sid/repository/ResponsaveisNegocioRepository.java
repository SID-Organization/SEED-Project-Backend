package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.ResponsaveisNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsaveisNegocioRepository extends JpaRepository<ResponsaveisNegocio, Integer> {
    
}
