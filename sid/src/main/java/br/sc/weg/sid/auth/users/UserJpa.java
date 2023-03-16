package br.sc.weg.sid.auth.users;

import br.sc.weg.sid.model.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserJpa implements UserDetails {

    @JsonIgnore
    Usuario usuario;

    private Collection<GrantedAuthority> authorities;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    public UserJpa(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String getPassword() {
        return usuario.getSenhaUsuario();
    }

    @Override
    public String getUsername() {
        return Integer.toString(usuario.getNumeroCadastroUsuario());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(
                this.getUsuario()
                        .getCargoUsuario().getNome()
        ));
        return authorities;
    }

}
