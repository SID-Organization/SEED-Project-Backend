package br.sc.weg.sid.auth.users;

import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.enums.Cargo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJpa implements UserDetails {

    @JsonIgnore
    Usuario usuario;

    private Collection<GrantedAuthority> authorities;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    private String password;

    private Integer username;

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

    public UserJpa(Usuario usuario){
        this.usuario = usuario;
        this.password = usuario.getSenhaUsuario();
        this.username = usuario.getNumeroCadastroUsuario();
        this.authorities = new ArrayList<>();
        this.authorities.add(new SimpleGrantedAuthority(usuario.getCargoUsuario().getNome()));
    }

}
