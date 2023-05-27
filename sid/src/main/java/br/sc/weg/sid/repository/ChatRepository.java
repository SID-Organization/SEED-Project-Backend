package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.enums.TamanhoDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query(value = "select * from chat, usuario_chat where usuario_chat.id_usuario = ?1 and usuario_chat.id_chat = chat.id_chat", nativeQuery = true)
    List<Chat> findChatByNumeroCadastroUsuario(Integer numeroCadastroUsuario);

}
