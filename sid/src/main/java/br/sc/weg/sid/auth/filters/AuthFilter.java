package br.sc.weg.sid.auth.filters;

import antlr.Token;
import br.sc.weg.sid.security.service.JpaService;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

//    private TokenUtils tokenUtils;

    private JpaService jpaService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/login")) {

        }
    }
}
