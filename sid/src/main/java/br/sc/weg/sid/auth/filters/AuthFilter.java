package br.sc.weg.sid.auth.filters;

import antlr.Token;
import br.sc.weg.sid.auth.service.JpaService;
import br.sc.weg.sid.auth.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private TokenUtils tokenUtils;

    private JpaService jpaService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/login/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = tokenUtils.buscarCookie(request, "jwt");
            Boolean valido = tokenUtils.validarToken(token);
            if (valido) {
                System.out.println("Token válido!");
                String usuarioNumeroCadastro = tokenUtils.getUsuarioNumeroCadastro(token);
                UserDetails usuario = jpaService.loadUserByUsername(usuarioNumeroCadastro);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario.getUsername(), null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        }
    }
}
