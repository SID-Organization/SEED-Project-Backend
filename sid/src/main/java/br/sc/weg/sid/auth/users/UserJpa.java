package br.sc.weg.sid.auth.users;

import br.sc.weg.sid.model.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserJpa implements UserDetails {

    @JsonIgnore
    Usuario usuario;

    public UserJpa(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return usuario.getSenhaUsuario();
    }

    @Override
    public String getUsername() {
        return Integer.toString(usuario.getNumeroCadastroUsuario());
    }

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;
}
