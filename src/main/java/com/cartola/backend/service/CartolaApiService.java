package com.cartola.backend.service;

import com.cartola.backend.dto.JogadorCartolaDTO;
import com.cartola.backend.dto.JogoDTO;
import com.cartola.backend.dto.ClubeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service("cartolaApiService")
public class CartolaApiService {

    private static final Logger logger = Logger.getLogger(CartolaApiService.class.getName());
    private static final String BASE_URL = "https://api.cartola.globo.com";
    
    @Autowired
    private PontuacaoService pontuacaoService;
    
    private final RestTemplate restTemplate;

    public CartolaApiService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Busca todos os jogadores da rodada atual do Cartola
     */
    public List<JogadorCartolaDTO> buscarJogadoresCartola() {
        String url = BASE_URL + "/atletas/pontuados?rodada=1";
        List<JogadorCartolaDTO> jogadores = new ArrayList<>();
        
        try {
            Map response = restTemplate.getForObject(url, Map.class);
            if (response == null || !response.containsKey("atletas")) {
                logger.warning("Resposta inválida da API do Cartola para jogadores");
                return jogadores;
            }
            
            Map<String, Object> atletas = (Map<String, Object>) response.get("atletas");
            for (Object value : atletas.values()) {
                Map<String, Object> atleta = (Map<String, Object>) value;
                JogadorCartolaDTO dto = mapearJogadorCartola(atleta);
                jogadores.add(dto);
            }
            
            logger.info("Busca de jogadores do Cartola concluída: " + jogadores.size() + " jogadores encontrados");
        } catch (Exception e) {
            logger.severe("Erro ao buscar jogadores do Cartola: " + e.getMessage());
            throw new RuntimeException("Falha ao buscar jogadores do Cartola", e);
        }
        
        return jogadores;
    }

    /**
     * Busca jogadores pontuados com pontuação personalizada
     */
    public List<JogadorCartolaDTO> buscarJogadoresPontuados() {
        String url = BASE_URL + "/atletas/pontuados";
        List<JogadorCartolaDTO> jogadores = new ArrayList<>();
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> response = responseEntity.getBody();
            
            if (response == null || !response.containsKey("atletas")) {
                logger.warning("Resposta inválida da API do Cartola para jogadores pontuados");
                return jogadores;
            }

            Map<String, Object> atletas = (Map<String, Object>) response.get("atletas");
            for (Object value : atletas.values()) {
                Map<String, Object> atleta = (Map<String, Object>) value;
                JogadorCartolaDTO dto = mapearJogadorCartola(atleta);
                
                // Aplicar pontuação personalizada
                Map<String, Object> scout = atleta.get("scout") instanceof Map ? 
                    (Map<String, Object>) atleta.get("scout") : null;
                double pontuacaoPersonalizada = pontuacaoService.calcularPontuacaoPersonalizada(scout);
                dto.setPontuacao(pontuacaoPersonalizada);
                
                jogadores.add(dto);
            }
            
            logger.info("Busca de jogadores pontuados concluída: " + jogadores.size() + " jogadores encontrados");
        } catch (Exception e) {
            logger.severe("Erro ao buscar jogadores pontuados: " + e.getMessage());
            throw new RuntimeException("Falha ao buscar jogadores pontuados", e);
        }
        
        return jogadores;
    }

    /**
     * Busca os jogos da rodada atual
     */
    public List<JogoDTO> buscarJogosRodada() {
        String url = BASE_URL + "/partidas";
        List<JogoDTO> jogos = new ArrayList<>();
        DateTimeFormatter formatterSaida = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        
        try {
            logger.info("Chamando API do Cartola: " + url);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> response = responseEntity.getBody();
            
            logger.info("Status HTTP: " + responseEntity.getStatusCode());
            
            if (response == null) {
                logger.warning("Resposta nula da API do Cartola");
                return jogos;
            }

            Object partidasObj = response.get("partidas");
            Object clubesObj = response.get("clubes");
            
            if (!(partidasObj instanceof List<?> partidas) || !(clubesObj instanceof Map<?, ?> clubes)) {
                logger.warning("Estrutura inesperada na resposta da API do Cartola");
                return jogos;
            }

            for (Object partidaObj : partidas) {
                if (!(partidaObj instanceof Map)) continue;
                
                Map<String, Object> partida = (Map<String, Object>) partidaObj;
                JogoDTO dto = mapearJogo(partida, clubes, formatterSaida);
                
                if (dto != null) {
                    jogos.add(dto);
                }
            }
            
            logger.info("Jogos decodificados: " + jogos.size());
        } catch (Exception e) {
            logger.severe("Erro ao buscar jogos da rodada: " + e.getMessage());
            throw new RuntimeException("Falha ao buscar jogos da rodada", e);
        }
        
        return jogos;
    }

    /**
     * Mapeia um objeto atleta da API para JogadorCartolaDTO
     */
    private JogadorCartolaDTO mapearJogadorCartola(Map<String, Object> atleta) {
        JogadorCartolaDTO dto = new JogadorCartolaDTO();
        dto.setId(atleta.get("atleta_id") instanceof Number ? 
            ((Number) atleta.get("atleta_id")).intValue() : 0);
        dto.setApelido(atleta.get("apelido") instanceof String ? 
            (String) atleta.get("apelido") : "");
        dto.setFoto(atleta.get("foto") instanceof String ? 
            (String) atleta.get("foto") : "");
        dto.setPosicaoId(atleta.get("posicao_id") instanceof Number ? 
            ((Number) atleta.get("posicao_id")).intValue() : 0);
        dto.setClubeId(atleta.get("clube_id") instanceof Number ? 
            ((Number) atleta.get("clube_id")).intValue() : 0);
        return dto;
    }

    /**
     * Mapeia um objeto partida da API para JogoDTO
     */
    private JogoDTO mapearJogo(Map<String, Object> partida, Map<?, ?> clubes, DateTimeFormatter formatterSaida) {
        Object partidaIdObj = partida.get("partida_id");
        Object casaIdObj = partida.get("clube_casa_id");
        Object visitanteIdObj = partida.get("clube_visitante_id");
        Object horarioObj = partida.get("partida_data");

        if (!(partidaIdObj instanceof Number) || 
            !(casaIdObj instanceof Number) || 
            !(visitanteIdObj instanceof Number)) {
            return null;
        }

        JogoDTO dto = new JogoDTO();
        dto.setPartida_id(((Number) partidaIdObj).intValue());
        
        int casaId = ((Number) casaIdObj).intValue();
        int visitanteId = ((Number) visitanteIdObj).intValue();

        // Formatar horário
        String horarioFormatado = formatarHorario(horarioObj, formatterSaida);
        dto.setHorario(horarioFormatado);

        // Mapear clubes
        ClubeDTO clubeCasa = mapearClube(casaId, clubes);
        ClubeDTO clubeVisitante = mapearClube(visitanteId, clubes);
        
        dto.setClube_casa(clubeCasa);
        dto.setClube_visitante(clubeVisitante);

        return dto;
    }

    /**
     * Formata o horário da partida
     */
    private String formatarHorario(Object horarioObj, DateTimeFormatter formatterSaida) {
        if (!(horarioObj instanceof String)) {
            return "";
        }

        String horarioStr = (String) horarioObj;
        try {
            // Tenta ISO 8601 com offset
            OffsetDateTime odt = OffsetDateTime.parse(horarioStr);
            return odt.format(formatterSaida);
        } catch (Exception ex1) {
            try {
                // Tenta yyyy-MM-dd HH:mm:ss
                DateTimeFormatter entradaSimples = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime ldt = LocalDateTime.parse(horarioStr, entradaSimples);
                return ldt.format(formatterSaida);
            } catch (Exception ex2) {
                return horarioStr; // Retorna string original se não conseguir formatar
            }
        }
    }

    /**
     * Mapeia informações do clube
     */
    private ClubeDTO mapearClube(int clubeId, Map<?, ?> clubes) {
        ClubeDTO clube = new ClubeDTO();
        clube.setId(clubeId);

        Object clubeObj = clubes.get(String.valueOf(clubeId));
        if (clubeObj instanceof Map) {
            Map<String, Object> clubeData = (Map<String, Object>) clubeObj;
            clube.setNome(clubeData.get("nome") instanceof String ? 
                (String) clubeData.get("nome") : "");
            
            Object escudosObj = clubeData.get("escudos");
            if (escudosObj instanceof Map) {
                clube.setEscudos((Map<String, String>) escudosObj);
            } else {
                clube.setEscudos(new HashMap<>());
            }
        } else {
            clube.setNome("");
            clube.setEscudos(new HashMap<>());
        }

        return clube;
    }
}