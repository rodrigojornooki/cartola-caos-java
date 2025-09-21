package com.cartola.backend.service;

import com.cartola.backend.model.Rodada;
import com.cartola.backend.repository.RodadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RodadaService {
    
    @Autowired
    private RodadaRepository rodadaRepository;

    public List<Rodada> listarTodas() {
        return rodadaRepository.findAll();
    }

    public Optional<Rodada> buscarPorId(Long id) {
        return rodadaRepository.findById(id);
    }

    public Optional<Rodada> buscarPorNumero(Integer numero) {
        return rodadaRepository.findByNumero(numero);
    }

    @Transactional
    public Rodada salvar(Rodada rodada) {
        return rodadaRepository.save(rodada);
    }

    @Transactional
    public void deletar(Long id) {
        rodadaRepository.deleteById(id);
    }
}