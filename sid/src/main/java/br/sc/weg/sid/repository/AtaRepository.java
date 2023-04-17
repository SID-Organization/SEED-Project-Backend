package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Ata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtaRepository extends JpaRepository<Ata, Integer> {
    Ata findByNumeroDgAta(Integer numeroDgAta);

//    List<Ata> findByPautaAta(Pauta pautaAta);
}
