package com.cartola.backend.service.integration;

import com.cartola.backend.dto.PontuacaoJogadorDTO;
import com.cartola.backend.dto.api.CartolaApiResponse;
import com.cartola.backend.dto.api.AtletaDTO;
import com.cartola.backend.model.ScoutNegativo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class CartolaApiService {
    
    @Autowired
    private WebClient webClient;

    @Scheduled(fixedRate = 300000) // 5 minutos
    public void atualizarPontuacoesAtual() {
        try {
            this.webClient.get()
                    .uri("/atletas/pontuados")
                    .retrieve()
                    .bodyToMono(CartolaApiResponse.class)
                    .subscribe(response -> {
                        if (response.getAtletas() != null) {
                            List<ScoutNegativo> scouts = response.getAtletas().values().stream()
                                    .map(this::processarScouts)
                                    .collect(Collectors.toList());

                            // TODO: Salvar scouts no banco de dados
                            System.out.println("Scouts processados: " + scouts.size());
                        }
                    });
        } catch (Exception e) {
            System.err.println("Erro ao buscar pontuações: " + e.getMessage());
        }
    }

    public List<ScoutNegativo> buscarPontuacoesAtual() {
        try {
            CartolaApiResponse response = webClient.get()
                    .uri("/atletas/pontuados")
                    .retrieve()
                    .bodyToMono(CartolaApiResponse.class)
                    .block();

            if (response != null && response.getAtletas() != null) {
                return response.getAtletas().values().stream()
                        .map(atleta -> {
                            ScoutNegativo scout = processarScouts(atleta);
                            scout.setAtletaId(atleta.getAtleta_id());
                            return scout;
                        })
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar pontuações atuais: " + e.getMessage());
        }
        return List.of();
    }

    @Cacheable(value = "mercado")
    public CartolaApiResponse buscarMercado() {
        try {
            return webClient.get()
                    .uri("/atletas/mercado")
                    .retrieve()
                    .bodyToMono(CartolaApiResponse.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Erro ao buscar mercado: " + e.getMessage());
            return null;
        }
    }

    public List<PontuacaoJogadorDTO> buscarPontuacoesRodada(Integer rodada) {
        try {
            CartolaApiResponse response = webClient.get()
                .uri("/atletas/pontuados?rodada=" + rodada)
                .retrieve()
                .bodyToMono(CartolaApiResponse.class)
                .block();
            if (response != null && response.getAtletas() != null) {
                return response.getAtletas().values().stream()
                    .map(this::mapearParaPontuacaoDTO)
                    .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar pontuações da rodada: " + e.getMessage());
        }
        return List.of();
    }

    private PontuacaoJogadorDTO mapearParaPontuacaoDTO(AtletaDTO atleta) {
        PontuacaoJogadorDTO dto = new PontuacaoJogadorDTO();
        dto.setAtleta_id(atleta.getAtleta_id());
        dto.setApelido(atleta.getApelido());
        dto.setPontuacao(atleta.getPontuacao());
        dto.setPosicao(atleta.getPosicao_id());
        dto.setClube(atleta.getClube_id());
        dto.setScouts(atleta.getScout());
        return dto;
    }

    public ScoutNegativo processarScouts(AtletaDTO atleta) {
        ScoutNegativo scout = new ScoutNegativo();
        Map<String, Object> scoutsObj = atleta.getScout();
        if (scoutsObj != null) {
            Map<String, Integer> scouts = scoutsObj.entrySet().stream()
                .filter(e -> e.getValue() instanceof Number)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> ((Number) e.getValue()).intValue()
                ));
            scout.setGolsContra(scouts.getOrDefault("GC", 0));
            scout.setCartaoVermelho(scouts.getOrDefault("CV", 0));
            scout.setCartaoAmarelo(scouts.getOrDefault("CA", 0));
            scout.setPenaltiPerdido(scouts.getOrDefault("PP", 0));
            scout.setImpedimentos(scouts.getOrDefault("I", 0));
            scout.setFaltas(scouts.getOrDefault("FC", 0));
            scout.setGolsSofridos(scouts.getOrDefault("GS", 0));
            scout.setFinalizacaoTrave(scouts.getOrDefault("FT", 0));
            scout.calcularPontuacaoTotal();
        }
        return scout;
    }

    public ScoutNegativo processarScouts(PontuacaoJogadorDTO dto) {
        ScoutNegativo scout = new ScoutNegativo();
        Map<String, Object> scoutsObj = dto.getScouts();
        if (scoutsObj != null) {
            Map<String, Integer> scouts = scoutsObj.entrySet().stream()
                .filter(e -> e.getValue() instanceof Number)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> ((Number) e.getValue()).intValue()
                ));
            scout.setGolsContra(scouts.getOrDefault("GC", 0));
            scout.setCartaoVermelho(scouts.getOrDefault("CV", 0));
            scout.setCartaoAmarelo(scouts.getOrDefault("CA", 0));
            scout.setPenaltiPerdido(scouts.getOrDefault("PP", 0));
            scout.setImpedimentos(scouts.getOrDefault("I", 0));
            scout.setFaltas(scouts.getOrDefault("FC", 0));
            scout.setGolsSofridos(scouts.getOrDefault("GS", 0));
            scout.setFinalizacaoTrave(scouts.getOrDefault("FT", 0));
            scout.calcularPontuacaoTotal();
        }
        return scout;
    }

    // Método para calcular pontuação do "Cartola do Caos"
    public Double calcularPontuacaoCaos(Double pontuacaoOriginal, ScoutNegativo scouts) {
        double pontuacao = 0.0;
        
        // Se o jogador não entrou em campo, retorna 0
        if (pontuacaoOriginal == null) {
            return 0.0;
        }
        
        // Pontuação base: pontuações negativas são convertidas em positivas
        if (pontuacaoOriginal < 0) {
            pontuacao += Math.abs(pontuacaoOriginal) * 2; // Bonus dobrado para pontuação negativa
        }
        
        // Adiciona a pontuação dos scouts negativos
        if (scouts != null) {
            pontuacao += scouts.getPontuacaoTotal();
        }
        
        return pontuacao;
    }

    public List<ScoutNegativo> buscarHistoricoJogador(Long jogadorId) {
        try {
            CartolaApiResponse response = webClient.get()
                    .uri("/atletas/" + jogadorId + "/pontuacao")
                    .retrieve()
                    .bodyToMono(CartolaApiResponse.class)
                    .block();

            if (response != null && response.getAtletas() != null) {
                AtletaDTO atleta = response.getAtletas().get(jogadorId.toString());
                if (atleta != null) {
                    ScoutNegativo scout = processarScouts(atleta);
                    scout.setAtletaId(jogadorId);
                    return List.of(scout);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar histórico do jogador: " + e.getMessage());
        }
        return List.of();
    }
}