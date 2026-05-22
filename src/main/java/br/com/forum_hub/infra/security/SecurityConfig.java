package br.com.forum_hub.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(sm -> // Configura o gerenciamento de sessão da aplicação
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define que a aplicação não criará nem usará sessões (stateless), comum em APIs REST que usam token (ex: JWT)
                .csrf(csrf -> csrf.disable()) // Desativa a proteção CSRF (Cross-Site Request Forgery). Geralmente desabilitado em APIs stateless que não usam cookies para autenticação
                .build(); // Constrói e retorna a cadeia de filtros de segurança configurada
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
        // Obtém a configuração de autenticação já definida pelo Spring Security (por exemplo, provedores de autentação, UserDetailsService, etc.)
        // Retorna o AuthenticationManager pronto, que será usado para processar autenticações (login), validando usuário e senha
    }

    @Bean
    public PasswordEncoder encriptador() {
        return new BCryptPasswordEncoder();
    }

}
