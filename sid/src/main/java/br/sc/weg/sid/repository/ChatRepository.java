package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    

}
