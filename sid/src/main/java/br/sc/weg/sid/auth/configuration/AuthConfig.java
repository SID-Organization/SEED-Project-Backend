package br.sc.weg.sid.auth.configuration;

import br.sc.weg.sid.auth.filters.AuthFilter;
import br.sc.weg.sid.auth.service.JpaService;
import br.sc.weg.sid.auth.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.bouncycastle.crypto.generators.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class AuthConfig {

    private JpaService jpaService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder amb) throws Exception {
        amb.userDetailsService(jpaService).passwordEncoder(new BCryptPasswordEncoder());
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8081", "http://127.0.0.1:8081"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Access-Control-Allow-Origin", "*", "XMLHttpRequest"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeRequests().anyRequest().permitAll();
//                .antMatchers("/login", "/login/auth", "/sid/api/usuario", "/sid/api/docs/**", "/swagger-ui/**", "/sid/swagger-ui.html").permitAll()
//                .antMatchers("/sid/api/demanda/**", "/sid/api/pdf-demanda/**", "sid/api/chat/**").hasAnyAuthority("Solicitante", "Gestor TI", "Gerente", "Analista")
//                .antMatchers("/sid/api/proposta/**", "sid/api/pdf-proposta/**", "sid/api/pauta", "sid/api/ata/**", "sid/api/forum/**", "sid/api/tabela-custo/**", "sid/api/historico-workflow/**", "sid/api/decisao-proposta/**", "sid/api/business-unity/**").hasAnyAuthority("Gestor TI", "Analista")
//                .anyRequest().authenticated();

        httpSecurity.csrf().disable().cors().configurationSource(corsConfigurationSource()).and().logout().deleteCookies("jwt", "user").permitAll();

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(new AuthFilter(new TokenUtils(), jpaService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration ac) throws Exception {
        return ac.getAuthenticationManager();
    }
}
