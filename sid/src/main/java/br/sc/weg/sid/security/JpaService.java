package br.sc.weg.sid.security;

import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.repository.UsuarioRepository;
import br.sc.weg.sid.security.users.UserJPA;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.Optional;

public class JpaService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    private final String senhaForte = "8d93c8a196a1d63c95815dffd7eb788f8596fc272770375d4109dc636100de11";

    @Override
    public UserDetails loadUserByUsername(String numeroCadastro) throws UsernameNotFoundException {
        System.out.println("Username: " + numeroCadastro);
        Optional<Usuario> userOptional = userRepository.findById(Integer.parseInt(numeroCadastro));
        if (userOptional.isPresent()) {
            System.out.println("Pessoa encontrada");
            return new UserJPA(userOptional.get());
        }
        System.out.println("Pessoa não encontrada");
        throw new UsernameNotFoundException("Usuário não encontrado");
    }


    public String gerarToken(Authentication authenticationToken) {
        Usuario usuario = (Usuario) authenticationToken.getPrincipal();
        return Jwts.builder()
                .setIssuer("API da Editora de Livros")
                .setSubject(usuario.getNumeroCadastroUsuario().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1800000))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, senhaForte)
                .compact();
    }

    public Boolean validaToken(String token) {
        try {
            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
            System.out.println("Token válido");
            return true;
        } catch (Exception e) {
            System.out.println("Token inválido");
            return false;
        }
    }

    public UserJPA getUsuario(String token) {
        String numeroCadastro = Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token).getBody().getSubject();
        System.out.println("CPF: " + numeroCadastro);
        return new UserJPA(userRepository.findById(Integer.parseInt(numeroCadastro)).get());
    }

}
