package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.ResponsaveisNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponsaveisNegocioRepository extends JpaRepository<ResponsaveisNegocio, Integer> {
    List<ResponsaveisNegocio> findAllByIdProposta(Proposta idProposta);
}
