package br.com.forum_hub.controller;

import br.com.forum_hub.domain.resposta.DadosAtualizacaoResposta;
import br.com.forum_hub.domain.resposta.DadosCadastroResposta;
import br.com.forum_hub.domain.resposta.DadosListagemResposta;
import br.com.forum_hub.domain.resposta.RespostaService;
import br.com.forum_hub.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("topicos/{idTopico}/respostas")
public class RespostaController {

    private final RespostaService service;

    public RespostaController(RespostaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DadosListagemResposta> cadastrar(@PathVariable Long idTopico,
                                                           @RequestBody @Valid DadosCadastroResposta dados,
                                                           UriComponentsBuilder uriBuilder,
                                                           @AuthenticationPrincipal Usuario usuarioLogado) {
        var resposta = service.cadastrar(dados, idTopico, usuarioLogado);
        var uri = uriBuilder.path("topicos/{idTopico}/respostas/{id}").buildAndExpand(resposta.getTopico().getId(), resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemResposta(resposta));
    }

    @PutMapping
    public ResponseEntity<DadosListagemResposta> atualizar(@RequestBody @Valid DadosAtualizacaoResposta dados,
                                                           @AuthenticationPrincipal Usuario logado){
        var resposta = service.atualizar(dados, logado);
        return ResponseEntity.ok(new DadosListagemResposta(resposta));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DadosListagemResposta> marcarComoSolucao(@PathVariable Long id,
                                                                   @AuthenticationPrincipal Usuario logado) {
        var resposta = service.marcarComoSolucao(id, logado);
        return ResponseEntity.ok(new DadosListagemResposta(resposta));
    }

    public ResponseEntity<Void> excluir(@PathVariable Long id,
                                        @AuthenticationPrincipal Usuario logado){
        service.excluir(id, logado);
        return ResponseEntity.noContent().build();
    }

}
