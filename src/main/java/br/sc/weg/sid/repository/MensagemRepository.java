package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.Mensagem;
import br.sc.weg.sid.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Integer> {

    List<Mensagem> findByIdChatAndIdUsuario(Chat idChat, Usuario idUsuario);

    List<Mensagem> findByIdChat(Chat idChat);
}
