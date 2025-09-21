package com.cartola.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cartola.backend.service.integration.CartolaApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import com.cartola.backend.model.ScoutNegativo;

@RestController
@RequestMapping("/api/cartola")
@Tag(name = "Cartola Negativo API", description = "Endpoints para o sistema Cartola Negativo")
public class CartolaController {

    @Autowired
    private CartolaApiService cartolaApiService;

    @GetMapping("/pontuacoes")
    @Operation(summary = "Buscar pontuações atuais", 
              description = "Retorna as pontuações negativas dos jogadores na rodada atual")
    public ResponseEntity<List<ScoutNegativo>> getPontuacoesAtuais() {
        List<ScoutNegativo> scouts = cartolaApiService.buscarPontuacoesAtual();
        return ResponseEntity.ok(scouts);
    }

    @GetMapping("/mercado/status")
    @Operation(summary = "Status do mercado", 
              description = "Retorna o status atual do mercado (aberto/fechado)")
    public ResponseEntity<String> getStatusMercado() {
        // TODO: Implementar retorno do status
        return ResponseEntity.ok().build();
    }

    @GetMapping("/jogadores/{id}/historico")
    @Operation(summary = "Histórico do jogador", 
              description = "Retorna o histórico de scouts negativos do jogador")
    public ResponseEntity<List<ScoutNegativo>> getHistoricoJogador(@PathVariable Long id) {
        // TODO: Implementar retorno do histórico
        return ResponseEntity.ok().build();
    }
}