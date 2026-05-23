package br.com.forum_hub.infra.security;

import br.com.forum_hub.domain.autenticacao.TokenService;
import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// OncePer
@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    public FiltroTokenAcesso(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Lógica para extrair e validar o token de acesso (JWT) do cabeçalho da requisição
        // Se o token for válido, configurar a autenticação no contexto de segurança do Spring Security
        // Se o token for inválido ou ausente, permitir que a requisição prossiga sem autenticação (ou retornar um erro, dependendo da configuração de segurança)

        // filterChain.doFilter(request, response); // Continua a cadeia de filtros, permitindo que a requisição seja processada pelos próximos filtros e eventualmente pelo controlador

        // recuperar o token de requisição
        String token = recuperarTokenRequisicao(request);

        if (token != null) {
            // validação do token
            String email = tokenService.verificarToken(token);
            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElseThrow();

            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); // Cria um objeto de autenticação do Spring Security usando os dados do usuário (email) extraídos do token JWT, sem fornecer credenciais (null) e com as autoridades (permissões) do usuário

            SecurityContextHolder.getContext().setAuthentication(authentication); // Configura o contexto de segurança do Spring Security com a autenticação criada, permitindo que a aplicação reconheça o usuário autenticado e suas permissões durante o processamento da requisição
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        // outra forma
//        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
//                ? authorizationHeader.substring(7)
//                : authorizationHeader;
        return authorizationHeader == null ? null : authorizationHeader.replace("Bearer ", "");
    }

}
