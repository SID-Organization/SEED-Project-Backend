package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Forum;
import br.sc.weg.sid.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Integer> {

//    @Query("select * from forum inner join usuarios_forum on forum.numero_cadastro_analista_responsavel = ?1")
//    Forum findByNumeroCadastroAnalistaResponsavel(Usuario numeroCadastroAnalistaResponsavel);

    List<Forum> findByAnalistaResponsavelForum(Usuario numeroCadastroAnalistaResponsavel);

}
