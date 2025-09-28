package com.cartola.backend.controller;

import com.cartola.backend.model.Jogador;
import com.cartola.backend.service.JogadorService;
import com.cartola.backend.service.CartolaApiService;
import com.cartola.backend.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.cartola.backend.dto.JogadorCartolaDTO;
import com.cartola.backend.dto.UsuarioDTO;
import com.cartola.backend.dto.JogoDTO;

@RestController
@RequestMapping("/api/jogadores")
@CrossOrigin(origins = "*")
public class JogadorController {
    
    @Autowired
    private JogadorService jogadorService;
    
    @Autowired
    private CartolaApiService cartolaApiService;
    
    @Autowired
    private ExternalApiService externalApiService;

    @GetMapping
    public List<Jogador> listarTodos() {
        return jogadorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogador> buscarPorId(@PathVariable Long id) {
        return jogadorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/time/{timeId}")
    public List<Jogador> listarPorTime(@PathVariable Long timeId) {
        return jogadorService.listarPorTime(timeId);
    }

    @GetMapping("/posicao/{posicao}")
    public List<Jogador> listarPorPosicao(@PathVariable String posicao) {
        return jogadorService.listarPorPosicao(posicao);
    }

    @PostMapping
    public Jogador criar(@RequestBody Jogador jogador) {
        return jogadorService.salvar(jogador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jogador> atualizar(@PathVariable Long id, @RequestBody Jogador jogador) {
        return jogadorService.buscarPorId(id)
                .map(jogadorExistente -> {
                    jogador.setId(id);
                    Jogador jogadorAtualizado = jogadorService.salvar(jogador);
                    return ResponseEntity.ok(jogadorAtualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (jogadorService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        jogadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cartola")
    public ResponseEntity<List<JogadorCartolaDTO>> listarJogadoresCartola() {
        try {
            List<JogadorCartolaDTO> jogadores = cartolaApiService.buscarJogadoresCartola();
            return ResponseEntity.ok(jogadores);
        } catch (Exception e) {
            return ResponseEntity.status(502).body(List.of());
        }
    }

    @GetMapping("/usuarios-teste")
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosTeste() {
        try {
            List<UsuarioDTO> usuarios = externalApiService.buscarUsuariosTeste();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(502).body(List.of());
        }
    }

    @GetMapping("/jogos-rodada")
    public ResponseEntity<List<JogoDTO>> listarJogosRodada() {
        try {
            List<JogoDTO> jogos = cartolaApiService.buscarJogosRodada();
            return ResponseEntity.ok(jogos);
        } catch (Exception e) {
            return ResponseEntity.status(502).body(List.of());
        }
    }

    @GetMapping("/pontuados")
    public ResponseEntity<List<JogadorCartolaDTO>> listarJogadoresPontuadosPersonalizados() {
        try {
            List<JogadorCartolaDTO> jogadores = cartolaApiService.buscarJogadoresPontuados();
            return ResponseEntity.ok(jogadores);
        } catch (Exception e) {
            return ResponseEntity.status(502).body(List.of());
        }
    }
}