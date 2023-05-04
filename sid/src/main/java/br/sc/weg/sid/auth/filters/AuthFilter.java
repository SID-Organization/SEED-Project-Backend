package br.sc.weg.sid.auth.filters;

import br.sc.weg.sid.auth.service.JpaService;
import br.sc.weg.sid.auth.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private TokenUtils tokenUtils;

    private JpaService jpaService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Iniciando filtro");
        if (request.getRequestURI().equals("/login") ||
                request.getRequestURI().equals("http://localhost:8443/sid/api/usuario/") ||
                request.getRequestURI().equals("/login/auth") ||
                request.getRequestURI().startsWith("/sid/api/docs") ||
                request.getRequestURI().startsWith("/sid/swagger-ui") ||
                request.getRequestURI().equals("/sid/swagger-ui.html") ||
                request.getRequestURI().equals("/favicon.ico") ||
                request.getRequestURI().equals("/sid/api/usuario") ||
                request.getRequestURI().equals("/sid/api/usuario/")
        ) {
            System.out.println("Entrou no primeiro if");
            filterChain.doFilter(request, response);
            return;
        }
        try {
            System.out.println("Entrou no try");
            String token = tokenUtils.buscarCookie(request, "jwt");
            Boolean valido = tokenUtils.validarToken(token);
            if (valido) {
                System.out.println("Token válido!");
                String usuarioNumeroCadastro = tokenUtils.getUsuarioNumeroCadastro(token);
                UserDetails usuario = jpaService.loadUserByUsername(usuarioNumeroCadastro);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario.getUsername(), null, usuario.getAuthorities());
                System.out.println("final do filtro");
                Cookie cookie = WebUtils.getCookie(request, "jwt");
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(1800);
                response.addCookie(cookie);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        }
    }
}
