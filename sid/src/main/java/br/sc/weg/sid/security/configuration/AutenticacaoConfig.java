package br.sc.weg.sid.security.configuration;

import br.sc.weg.sid.security.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AutenticacaoConfig {
    @Autowired
    private JpaService jpaService;

    //Configura as autorizações de acesso
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(jpaService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());


        httpSecurity.authenticationProvider(authenticationProvider);

        httpSecurity
                .authorizeRequests()
                //Libera o acesso sem autenticação no /login
                .antMatchers("/login").permitAll()
                //Determina que todas as demais requisições terão que ser autenticadas
                .anyRequest().authenticated()
                .and().csrf().disable()
                .formLogin().permitAll()
                .loginPage("/login")
                .defaultSuccessUrl("/demandas")
                .loginPage("/editoraLivros/login")
                .and()
                .logout()
                .logoutUrl("/editoraLivros/logout")
                .logoutSuccessUrl("/editoraLivros/login")
                .permitAll();
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().addFilterBefore(new AutenticacaoFiltro(jpaService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
