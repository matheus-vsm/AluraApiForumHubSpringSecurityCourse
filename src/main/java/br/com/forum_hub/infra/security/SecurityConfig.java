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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final FiltroTokenAcesso filtroTokenAcesso;

    public SecurityConfig(FiltroTokenAcesso filtroTokenAcesso) {
        this.filtroTokenAcesso = filtroTokenAcesso;
    }

    @Bean
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/login", "/atualizar-token",
                            "/registrar", "/verificar-conta").permitAll();
                    req.anyRequest().authenticated();
                })
                .sessionManagement(sm -> // Configura o gerenciamento de sessão da aplicação
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define que a aplicação não criará nem usará sessões (stateless), comum em APIs REST que usam token (ex: JWT)
                .csrf(csrf -> csrf.disable()) // Desativa a proteção CSRF (Cross-Site Request Forgery). Geralmente desabilitado em APIs stateless que não usam cookies para autenticação
                .addFilterBefore(filtroTokenAcesso, // Adiciona o filtro personalizado de autenticação por token (FiltroTokenAcesso) antes do filtro de autenticação padrão do Spring Security)
                        UsernamePasswordAuthenticationFilter.class) // Especifica que o filtro de token deve ser executado antes do filtro de autenticação de username e password
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
