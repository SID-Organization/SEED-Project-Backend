package br.sc.weg.sid.auth.controller;

import br.sc.weg.sid.auth.DTO.UsuarioDTO;
import br.sc.weg.sid.auth.users.UserJpa;
import br.sc.weg.sid.auth.utils.TokenUtils;
import br.sc.weg.sid.model.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class AuthController {

    private TokenUtils tokenUtils = new TokenUtils();

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<Object> autenticacao(
            @RequestBody @Valid UsuarioDTO usuarioDTO, HttpServletResponse response){
        UsernamePasswordAuthenticationToken dadosLogin =
                new UsernamePasswordAuthenticationToken(usuarioDTO.getUsername(), usuarioDTO.getSenha());
        try{
            Authentication authentication = authenticationManager.authenticate(dadosLogin);
            String token = tokenUtils.gerarToken(authentication);
            Cookie cookie = new Cookie("jwt", token);
            UserJpa userJPA = (UserJpa) authentication.getPrincipal();
            Usuario usuario = userJPA.getUsuario();
            response.addCookie(cookie);
            return ResponseEntity.ok().body(usuario);
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
