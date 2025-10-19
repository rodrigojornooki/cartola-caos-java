package com.cartola.backend.controller;

import com.cartola.backend.model.Rodada;
import com.cartola.backend.service.RodadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;


@RestController
@RequestMapping("/api/rodadas")
@CrossOrigin(origins = "*")
@Tag(name = "Rodadas", description = "Endpoints para gerenciamento de rodadas do campeonato")
public class RodadaController {


    @Autowired
    private RodadaService rodadaService;

    @GetMapping
    @Operation(summary = "Listar todas as rodadas", description = "Retorna a lista completa de rodadas do campeonato")
    @ApiResponse(responseCode = "200", description = "Lista de rodadas retornada com sucesso")
    public List<Rodada> listarTodas() {
        return rodadaService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar rodada por ID", description = "Retorna os detalhes de uma rodada específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rodada encontrada"),
        @ApiResponse(responseCode = "404", description = "Rodada não encontrada")
    })
    public ResponseEntity<Rodada> buscarPorId(
            @Parameter(description = "ID da rodada", required = true) @PathVariable Long id) {
        return rodadaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Buscar rodada por número", description = "Retorna uma rodada pelo seu número (1-38)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rodada encontrada"),
        @ApiResponse(responseCode = "404", description = "Rodada não encontrada")
    })
    public ResponseEntity<Rodada> buscarPorNumero(
            @Parameter(description = "Número da rodada", required = true, example = "15") @PathVariable Integer numero) {
        return rodadaService.buscarPorNumero(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar nova rodada", description = "Cadastra uma nova rodada no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rodada criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Rodada criar(@RequestBody Rodada rodada) {
        return rodadaService.salvar(rodada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar rodada", description = "Atualiza os dados de uma rodada existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rodada atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Rodada não encontrada")
    })
    public ResponseEntity<Rodada> atualizar(
            @Parameter(description = "ID da rodada", required = true) @PathVariable Long id,
            @RequestBody Rodada rodada) {
        return rodadaService.buscarPorId(id)
                .map(rodadaExistente -> {
                    rodada.setId(id);
                    Rodada rodadaAtualizada = rodadaService.salvar(rodada);
                    return ResponseEntity.ok(rodadaAtualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar rodada", description = "Remove uma rodada do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Rodada deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Rodada não encontrada")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da rodada", required = true) @PathVariable Long id) {
        if (!rodadaService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        rodadaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}