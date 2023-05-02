package br.sc.weg.sid.auth.controller;

import br.sc.weg.sid.auth.DTO.UsuarioDTO;
import br.sc.weg.sid.auth.service.JpaService;
import br.sc.weg.sid.auth.users.UserJpa;
import br.sc.weg.sid.auth.utils.TokenUtils;
import br.sc.weg.sid.model.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JpaService jpaService;

    private TokenUtils tokenUtils = new TokenUtils();

    /**
     * Esta função é um mapeamento de requisição HTTP POST que autentica um usuário no sistema.
     * @param usuarioDTO - Objeto DTO que contém os dados do usuário a ser autenticado.
     * @param response - Objeto HttpServletResponse que contém a resposta da requisição.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o objeto Usuario autenticado.
     * @throws AuthenticationException - Retorna uma mensagem de erro caso o usuário não seja autenticado.
     * @throws Exception - Retorna uma mensagem de erro caso ocorra algum erro na autenticação do usuário.
     */
    @PostMapping("/auth")
    public ResponseEntity<Object> autenticar(
            @RequestBody @Valid UsuarioDTO usuarioDTO,
            HttpServletResponse response
    ) {
        System.out.println("usuarioDTO.getUsername() = " + usuarioDTO.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getUsername(), usuarioDTO.getSenha());

        try {
            Authentication authentication = authManager.authenticate(authenticationToken);
            String token = tokenUtils.gerarToken(authentication);
            Cookie cookie = new Cookie("jwt", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            System.out.println(cookie.getValue());
            UserJpa user = (UserJpa) authentication.getPrincipal();
            Usuario pessoa = user.getUsuario();
            System.out.println(user.getAuthorities());
            System.out.println(cookie.getValue());
            return ResponseEntity.status(HttpStatus.OK).body(pessoa);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos!");
        }
    }
}
