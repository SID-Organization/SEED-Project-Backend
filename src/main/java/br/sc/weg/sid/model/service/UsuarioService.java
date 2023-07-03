package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.enums.Cargo;
import br.sc.weg.sid.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public <S extends Usuario> S saveAndFlush(S entity) {
        return usuarioRepository.saveAndFlush(entity);
    }

    public <S extends Usuario> S save(S entity) {
        return usuarioRepository.save(entity);
    }

    public Optional<Usuario> findById(Integer integer) {
        return usuarioRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return usuarioRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        usuarioRepository.deleteById(integer);
    }

    public Optional<Usuario> findByNumeroCadastroUsuario(Integer numeroCadastroUsuario) {
        return usuarioRepository.findByNumeroCadastroUsuario(numeroCadastroUsuario);
    }

    public List<Usuario> findByCargoUsuario(Cargo cargoUsuario) {
        return usuarioRepository.findByCargoUsuario(cargoUsuario);
    }

    public List<Usuario> findAllByDepartamentoUsuario(String departamentoUsuario) {
        return usuarioRepository.findAllByDepartamentoUsuario(departamentoUsuario);
    }
}
