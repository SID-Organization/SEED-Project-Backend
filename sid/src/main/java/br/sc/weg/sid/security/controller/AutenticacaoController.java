package br.sc.weg.sid.security.controller;

import br.sc.weg.sid.security.DTO.TokenDTO;
import br.sc.weg.sid.security.JpaService;
import br.sc.weg.sid.security.DTO.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JpaService jpaService;

    @PostMapping
    public ResponseEntity<Object> autenticar(
            @RequestBody @Valid UsuarioDTO usuarioDTO
    ) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getNumeroCadastroUsuario(), usuarioDTO.getSenhaUsuario());

        try {
            Authentication authentication = authManager.authenticate(authenticationToken);
            String token = jpaService.gerarToken(authentication);
            return ResponseEntity.ok().body("Login do usuário efetuado com sucesso!"
                    + "\n" + new TokenDTO("Bearer", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Usuário ou senha inválidos!");
        }
    }
}
