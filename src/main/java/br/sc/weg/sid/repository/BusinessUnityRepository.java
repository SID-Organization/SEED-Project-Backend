package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.BusinessUnity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessUnityRepository extends JpaRepository<BusinessUnity, Integer> {
    Optional<BusinessUnity> findByNomeBusinessUnity(String nomeBusinessUnity);
}
