package br.com.forum_hub.controller;

import br.com.forum_hub.domain.autenticacao.DadosLogin;
import br.com.forum_hub.domain.autenticacao.TokenService;
import br.com.forum_hub.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Validated
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> efetuarLogin(@Valid @RequestBody DadosLogin dados) {
        var autenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(autenticationToken); // Executa o processo de autenticação usando o token (email e senha), retornando um objeto Authentication com os dados do usuário autenticado

        String tokenAcesso = tokenService.gerarTokenJwt((Usuario) authentication.getPrincipal()); // Gera um token JWT de acesso usando os dados do usuário autenticado (authentication.getPrincipal())

        return ResponseEntity.ok(tokenAcesso);
    }

}
