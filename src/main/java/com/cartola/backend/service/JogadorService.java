package com.cartola.backend.service;

import com.cartola.backend.model.Jogador;
import com.cartola.backend.model.ScoutNegativo;
import com.cartola.backend.repository.JogadorRepository;
import com.cartola.backend.service.integration.CartolaApiIntegrationService;
import com.cartola.backend.dto.PontuacaoJogadorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JogadorService {
    
    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private CartolaApiIntegrationService cartolaApiService;

    public List<Jogador> listarTodos() {
        return jogadorRepository.findAll();
    }

    public List<Jogador> listarPorTime(Long timeId) {
        return jogadorRepository.findByTimeId(timeId);
    }

    public List<Jogador> listarPorPosicao(String posicao) {
        return jogadorRepository.findByPosicao(posicao);
    }

    public Optional<Jogador> buscarPorId(Long id) {
        return jogadorRepository.findById(id);
    }

    @Transactional
    public Jogador salvar(Jogador jogador) {
        // Validação para garantir que só jogadores válidos sejam salvos
        if (jogador.getPontuacao() != null && jogador.getPontuacao() <= 0) {
            throw new IllegalArgumentException("Apenas jogadores com pontuação positiva podem ser escalados");
        }
        return jogadorRepository.save(jogador);
    }

    @Transactional
    public void deletar(Long id) {
        jogadorRepository.deleteById(id);
    }

    public List<Jogador> atualizarPontuacoesRodada(Integer rodada) {
        List<PontuacaoJogadorDTO> pontuacoes = cartolaApiService.buscarPontuacoesRodada(rodada);
        
        return pontuacoes.stream()
            .filter(p -> p.getPontuacao() != null) // Filtra apenas jogadores que entraram em campo
            .map(p -> {
                Optional<Jogador> jogadorOpt = jogadorRepository.findById(p.getAtleta_id());
                Jogador jogador = jogadorOpt.orElseGet(() -> {
                    Jogador novo = new Jogador();
                    novo.setId(p.getAtleta_id());
                    novo.setNome(p.getApelido());
                    novo.setPosicao(p.getPosicao());
                    return novo;
                });
                
                // Processa os scouts negativos e calcula a pontuação do caos
                ScoutNegativo scouts = cartolaApiService.processarScouts(p);
                double pontuacaoCaos = cartolaApiService.calcularPontuacaoCaos(p.getPontuacao(), scouts);
                jogador.setPontuacao(pontuacaoCaos);
                
                return jogadorRepository.save(jogador);
            })
            .collect(Collectors.toList());
    }

    public double calcularPontuacaoTotalTime(Long timeId) {
        List<Jogador> jogadoresTime = listarPorTime(timeId);
        return jogadoresTime.stream()
                .filter(j -> j.getPontuacao() != null && j.getPontuacao() > 0)
                .mapToDouble(Jogador::getPontuacao)
                .sum();
    }
}