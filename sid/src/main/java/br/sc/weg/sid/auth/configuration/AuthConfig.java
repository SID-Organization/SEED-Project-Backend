package br.sc.weg.sid.auth.configuration;

import br.sc.weg.sid.auth.filters.AuthFilter;
import br.sc.weg.sid.auth.service.JpaService;
import br.sc.weg.sid.auth.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class AuthConfig {

    private JpaService jpaService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder amb) throws Exception {
        amb.userDetailsService(jpaService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/login", "/login/auth").permitAll()
                .anyRequest().authenticated();
        httpSecurity.csrf().disable()
                .cors().disable();

        httpSecurity.sessionManagement().sessionCreationPolicy(
                SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(new AuthFilter(new TokenUtils(), jpaService),
                UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration ac) throws Exception {
        return ac.getAuthenticationManager();
    }
}
