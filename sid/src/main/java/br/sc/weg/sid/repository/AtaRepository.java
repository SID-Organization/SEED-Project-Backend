package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtaRepository extends JpaRepository<Ata, Integer> {
    Ata findByNumeroDgAta(Integer numeroDgAta);

    List<Ata> findByPautaAta(Pauta pautaAta);
}
