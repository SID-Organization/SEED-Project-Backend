package br.sc.weg.sid.model.service;

public class UsuarioService {
<<<<<<< Updated upstream
=======
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

    public List<Usuario> findAllByDepartamentoUsuario(String departamentoUsuario) {
        return usuarioRepository.findAllByDepartamentoUsuario(departamentoUsuario);
    }
>>>>>>> Stashed changes
}
