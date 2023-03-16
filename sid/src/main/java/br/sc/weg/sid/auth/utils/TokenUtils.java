package br.sc.weg.sid.auth.utils;

import br.sc.weg.sid.auth.users.UserJpa;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Jwts;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


public class TokenUtils {
    private final String senhaForte = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";

    public String gerarToken(Authentication authentication){
        UserJpa userJpa = (UserJpa) authentication.getPrincipal();
        return Jwts.builder()
                .setIssuer("Seed Application")
                .setSubject(userJpa.getUsuario().getNumeroCadastroUsuario().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1800000))
                .signWith(SignatureAlgorithm.HS256, senhaForte)
                .compact();
    }

    public Boolean validarToken(String token){
        try{
            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsuarioNumeroCadastro(String token) {
        return Jwts.parser()
                .setSigningKey(senhaForte)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public String buscarCookie(HttpServletRequest request, String nomeCookie){
        Cookie cookie = WebUtils.getCookie(request, nomeCookie);
        if (cookie != null){
            return cookie.getValue();
        }
        throw new RuntimeException("Cookie não encontrado");
    }

}
