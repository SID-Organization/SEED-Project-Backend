package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.DecisaoProposta;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecisaoPropostaRepository extends JpaRepository<DecisaoProposta, Integer> {

    List<DecisaoProposta> findByIdAta(Ata idAta);
    List<DecisaoProposta> findByIdPauta(Pauta idPauta);
    List<DecisaoProposta> findByIdProposta(Proposta idProposta);

}
