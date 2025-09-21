package com.cartola.backend.controller;

import com.cartola.backend.model.Time;
import com.cartola.backend.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/times")
@CrossOrigin(origins = "*")
public class TimeController {
    
    @Autowired
    private TimeService timeService;

    @GetMapping
    public List<Time> listarTodos() {
        return timeService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Time> buscarPorId(@PathVariable Long id) {
        return timeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{nomeUsuario}")
    public ResponseEntity<Time> buscarPorNomeUsuario(@PathVariable String nomeUsuario) {
        return timeService.buscarPorNomeUsuario(nomeUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Time criar(@RequestBody Time time) {
        return timeService.salvar(time);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Time> atualizar(@PathVariable Long id, @RequestBody Time time) {
        return timeService.buscarPorId(id)
                .map(timeExistente -> {
                    time.setId(id);
                    Time timeAtualizado = timeService.salvar(time);
                    return ResponseEntity.ok(timeAtualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!timeService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        timeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}