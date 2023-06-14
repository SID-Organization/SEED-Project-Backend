package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Ata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtaRepository extends JpaRepository<Ata, Integer> {

//    List<Ata> findByPautaAta(Pauta pautaAta);

}
