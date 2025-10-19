package com.cartola.backend.controller;

import com.cartola.backend.model.Jogador;
import com.cartola.backend.service.JogadorService;
import com.cartola.backend.service.CartolaApiService;
import com.cartola.backend.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;
import com.cartola.backend.dto.JogadorCartolaDTO;
import com.cartola.backend.dto.UsuarioDTO;
import com.cartola.backend.dto.JogoDTO;

@RestController
@RequestMapping("/api/jogadores")
@CrossOrigin(origins = "*")
@Tag(name = "Jogadores", description = "Endpoints para gerenciamento de jogadores")
public class JogadorController {
    
    @Autowired
    private JogadorService jogadorService;
    
    @Autowired
    private CartolaApiService cartolaApiService;
    
    @Autowired
    private ExternalApiService externalApiService;

    @GetMapping
    @Operation(summary = "Listar todos os jogadores", description = "Retorna a lista completa de jogadores cadastrados no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de jogadores retornada com sucesso")
    public List<Jogador> listarTodos() {
        return jogadorService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar jogador por ID", description = "Retorna os detalhes de um jogador específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Jogador encontrado"),
        @ApiResponse(responseCode = "404", description = "Jogador não encontrado")
    })
    public ResponseEntity<Jogador> buscarPorId(
            @Parameter(description = "ID do jogador", required = true) @PathVariable Long id) {
        return jogadorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/time/{timeId}")
    @Operation(summary = "Listar jogadores por time", description = "Retorna todos os jogadores de um time específico")
    @ApiResponse(responseCode = "200", description = "Lista de jogadores do time")
    public List<Jogador> listarPorTime(
            @Parameter(description = "ID do time", required = true) @PathVariable Long timeId) {
        return jogadorService.listarPorTime(timeId);
    }

    @GetMapping("/posicao/{posicao}")
    @Operation(summary = "Listar jogadores por posição", description = "Retorna todos os jogadores de uma posição específica (GOL, ZAG, LAT, MEI, ATA)")
    @ApiResponse(responseCode = "200", description = "Lista de jogadores da posição")
    public List<Jogador> listarPorPosicao(
            @Parameter(description = "Posição do jogador", required = true, example = "ATA") @PathVariable String posicao) {
        return jogadorService.listarPorPosicao(posicao);
    }

    @PostMapping
    @Operation(summary = "Criar novo jogador", description = "Cadastra um novo jogador no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Jogador criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Jogador criar(@RequestBody Jogador jogador) {
        return jogadorService.salvar(jogador);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar jogador", description = "Atualiza os dados de um jogador existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Jogador atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Jogador não encontrado")
    })
    public ResponseEntity<Jogador> atualizar(
            @Parameter(description = "ID do jogador", required = true) @PathVariable Long id,
            @RequestBody Jogador jogador) {
        return jogadorService.buscarPorId(id)
                .map(jogadorExistente -> {
                    jogador.setId(id);
                    Jogador jogadorAtualizado = jogadorService.salvar(jogador);
                    return ResponseEntity.ok(jogadorAtualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar jogador", description = "Remove um jogador do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Jogador deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Jogador não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do jogador", required = true) @PathVariable Long id) {
        if (jogadorService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        jogadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cartola")
    @Operation(summary = "Listar jogadores do Cartola FC", description = "Busca os jogadores disponíveis na API oficial do Cartola FC")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de jogadores retornada com sucesso"),
        @ApiResponse(responseCode = "502", description = "Erro ao buscar dados da API externa")
    })
    public ResponseEntity<List<JogadorCartolaDTO>> listarJogadoresCartola() {
        try {
            List<JogadorCartolaDTO> jogadores = cartolaApiService.buscarJogadoresCartola();
            return ResponseEntity.ok(jogadores);
        } catch (Exception e) {
            return ResponseEntity.status(502).body(List.of());
        }
    }

    @GetMapping("/usuarios-teste")
    @Operation(summary = "Listar usuários de teste", description = "Endpoint de teste para validar integração com API externa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada"),
        @ApiResponse(responseCode = "502", description = "Erro ao buscar dados da API externa")
    })
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosTeste() {
        try {
            List<UsuarioDTO> usuarios = externalApiService.buscarUsuariosTeste();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(502).body(List.of());
        }
    }

    @GetMapping("/jogos-rodada")
    @Operation(summary = "Listar jogos da rodada", description = "Retorna os jogos da rodada atual do Campeonato Brasileiro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de jogos retornada com sucesso"),
        @ApiResponse(responseCode = "502", description = "Erro ao buscar dados da API do Cartola FC")
    })
    public ResponseEntity<List<JogoDTO>> listarJogosRodada() {
        try {
            List<JogoDTO> jogos = cartolaApiService.buscarJogosRodada();
            return ResponseEntity.ok(jogos);
        } catch (Exception e) {
            return ResponseEntity.status(502).body(List.of());
        }
    }

    @GetMapping("/pontuados")
    @Operation(summary = "Listar jogadores pontuados", description = "Retorna jogadores com pontuação personalizada")
    @ApiResponse(responseCode = "200", description = "Lista de jogadores pontuados retornada com sucesso")
    public ResponseEntity<List<JogadorCartolaDTO>> listarJogadoresPontuadosPersonalizados() {
        try {
            List<JogadorCartolaDTO> jogadores = cartolaApiService.buscarJogadoresPontuados();
            return ResponseEntity.ok(jogadores);
        } catch (Exception e) {
            return ResponseEntity.status(502).body(List.of());
        }
    }
}