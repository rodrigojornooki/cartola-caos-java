package com.cartola.backend.controller;

import com.cartola.backend.model.Time;
import com.cartola.backend.service.TimeService;
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
@RequestMapping("/api/times")
@CrossOrigin(origins = "*")
@Tag(name = "Times", description = "Endpoints para gerenciamento de times dos usuários")
public class TimeController {
    
    @Autowired
    private TimeService timeService;

    @GetMapping
    @Operation(summary = "Listar todos os times", description = "Retorna a lista completa de times cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de times retornada com sucesso")
    public List<Time> listarTodos() {
        return timeService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar time por ID", description = "Retorna os detalhes de um time específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time encontrado"),
        @ApiResponse(responseCode = "404", description = "Time não encontrado")
    })
    public ResponseEntity<Time> buscarPorId(
            @Parameter(description = "ID do time", required = true) @PathVariable Long id) {
        return timeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{nomeUsuario}")
    @Operation(summary = "Buscar time por nome de usuário", description = "Retorna o time associado a um nome de usuário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time encontrado"),
        @ApiResponse(responseCode = "404", description = "Time não encontrado")
    })
    public ResponseEntity<Time> buscarPorNomeUsuario(
            @Parameter(description = "Nome do usuário", required = true, example = "rodrigo_cartola") @PathVariable String nomeUsuario) {
        return timeService.buscarPorNomeUsuario(nomeUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo time", description = "Cadastra um novo time no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Time criar(@RequestBody Time time) {
        return timeService.salvar(time);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar time", description = "Atualiza os dados de um time existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Time não encontrado")
    })
    public ResponseEntity<Time> atualizar(
            @Parameter(description = "ID do time", required = true) @PathVariable Long id,
            @RequestBody Time time) {
        return timeService.buscarPorId(id)
                .map(timeExistente -> {
                    time.setId(id);
                    Time timeAtualizado = timeService.salvar(time);
                    return ResponseEntity.ok(timeAtualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar time", description = "Remove um time do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Time deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Time não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do time", required = true) @PathVariable Long id) {
        if (!timeService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        timeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}