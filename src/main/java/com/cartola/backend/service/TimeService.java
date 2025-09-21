package com.cartola.backend.service;

import com.cartola.backend.model.Time;
import com.cartola.backend.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TimeService {
    
    @Autowired
    private TimeRepository timeRepository;

    public List<Time> listarTodos() {
        return timeRepository.findAll();
    }

    public Optional<Time> buscarPorId(Long id) {
        return timeRepository.findById(id);
    }

    public Optional<Time> buscarPorNomeUsuario(String nomeUsuario) {
        return timeRepository.findByNomeUsuario(nomeUsuario);
    }

    @Transactional
    public Time salvar(Time time) {
        return timeRepository.save(time);
    }

    @Transactional
    public void deletar(Long id) {
        timeRepository.deleteById(id);
    }
}