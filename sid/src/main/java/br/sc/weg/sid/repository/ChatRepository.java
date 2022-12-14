package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Chat;
import br.sc.weg.sid.model.entities.Demanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query(value = "select * from chat c right join usuario_chat u on u.id_usuario = ?1", nativeQuery = true)
    List<Chat> findChatByNumeroCadastroUsuario(Integer numeroCadastroUsuario);
}
