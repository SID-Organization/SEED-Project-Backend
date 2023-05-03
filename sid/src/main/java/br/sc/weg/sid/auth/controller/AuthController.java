package br.sc.weg.sid.auth.controller;

import br.sc.weg.sid.auth.DTO.UsuarioDTO;
import br.sc.weg.sid.auth.service.JpaService;
import br.sc.weg.sid.auth.users.UserJpa;
import br.sc.weg.sid.auth.utils.TokenUtils;
import br.sc.weg.sid.model.entities.Usuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = URLEncoder.encode(
                    objectMapper.writeValueAsString(user.getUsuario()),
                    StandardCharsets.UTF_8);
            Cookie userCookie = new Cookie("user", userJson);
            userCookie.setPath("/");
            response.addCookie(userCookie);
            Usuario pessoa = user.getUsuario();
            System.out.println(user.getAuthorities());
            System.out.println(cookie.getValue());
            return ResponseEntity.ok().body("Usuário autenticado com sucesso!");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos!");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/success")
    public RedirectView localRedirect() {
        System.out.println("success");
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8081/login");
        return redirectView;
    }

}
