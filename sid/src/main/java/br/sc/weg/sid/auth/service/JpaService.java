package br.sc.weg.sid.auth.service;

import br.sc.weg.sid.auth.users.UserJpa;
import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String numeroDeCadastroString) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional;
        try{
            Integer numeroDeCadastro = Integer.parseInt(numeroDeCadastroString);
            usuarioOptional = usuarioRepository.findById(numeroDeCadastro);
        }catch(Exception e){
            throw new UsernameNotFoundException("Erro ao buscar usuário! " + e.getMessage());
        }
        if (usuarioOptional.isPresent()) {
            return new UserJpa(usuarioOptional.get());
        }
        throw new UsernameNotFoundException("Usuário não encontrado!");
    }
}
