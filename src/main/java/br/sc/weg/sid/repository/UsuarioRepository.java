package br.sc.weg.sid.repository;

import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.enums.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNumeroCadastroUsuario(Integer numeroCadastroUsuario);

    List<Usuario> findByCargoUsuario(Cargo cargoUsuario);

    List<Usuario> findAllByDepartamentoUsuario(String departamentoUsuario);
}
