package com.cartola.backend.controller;

import com.cartola.backend.model.Rodada;
import com.cartola.backend.service.RodadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/rodadas")
@CrossOrigin(origins = "*")
public class RodadaController {


    @Autowired
    private RodadaService rodadaService;

    @GetMapping
    public List<Rodada> listarTodas() {
        return rodadaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rodada> buscarPorId(@PathVariable Long id) {
        return rodadaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<Rodada> buscarPorNumero(@PathVariable Integer numero) {
        return rodadaService.buscarPorNumero(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Rodada criar(@RequestBody Rodada rodada) {
        return rodadaService.salvar(rodada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rodada> atualizar(@PathVariable Long id, @RequestBody Rodada rodada) {
        return rodadaService.buscarPorId(id)
                .map(rodadaExistente -> {
                    rodada.setId(id);
                    Rodada rodadaAtualizada = rodadaService.salvar(rodada);
                    return ResponseEntity.ok(rodadaAtualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!rodadaService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        rodadaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}