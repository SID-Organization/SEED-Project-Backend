package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Notificacao;
import br.sc.weg.sid.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {

    List<Notificacao> findByUsuario(Usuario usuario);

}
