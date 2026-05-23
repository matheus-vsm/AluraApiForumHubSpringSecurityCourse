package br.com.forum_hub.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// OncePer
@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Lógica para extrair e validar o token de acesso (JWT) do cabeçalho da requisição
        // Se o token for válido, configurar a autenticação no contexto de segurança do Spring Security
        // Se o token for inválido ou ausente, permitir que a requisição prossiga sem autenticação (ou retornar um erro, dependendo da configuração de segurança)

        // filterChain.doFilter(request, response); // Continua a cadeia de filtros, permitindo que a requisição seja processada pelos próximos filtros e eventualmente pelo controlador

        // recuperar o token de requisição
        String token;
        if (token != null) {
            // validação do token

        }

        filterChain.doFilter(request, response);
    }

}
