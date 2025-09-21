package com.cartola.backend.controller;

import com.cartola.backend.model.Jogador;
import com.cartola.backend.service.JogadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import com.cartola.backend.dto.JogadorCartolaDTO;
import com.cartola.backend.dto.UsuarioDTO;
import com.cartola.backend.dto.ClubeDTO;
import com.cartola.backend.dto.JogoDTO;

@RestController
@RequestMapping("/api/jogadores")
@CrossOrigin(origins = "*")
public class JogadorController {
    
    @Autowired
    private JogadorService jogadorService;

    @GetMapping
    public List<Jogador> listarTodos() {
        return jogadorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogador> buscarPorId(@PathVariable Long id) {
        return jogadorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/time/{timeId}")
    public List<Jogador> listarPorTime(@PathVariable Long timeId) {
        return jogadorService.listarPorTime(timeId);
    }

    @GetMapping("/posicao/{posicao}")
    public List<Jogador> listarPorPosicao(@PathVariable String posicao) {
        return jogadorService.listarPorPosicao(posicao);
    }

    @PostMapping
    public Jogador criar(@RequestBody Jogador jogador) {
        return jogadorService.salvar(jogador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jogador> atualizar(@PathVariable Long id, @RequestBody Jogador jogador) {
        return jogadorService.buscarPorId(id)
                .map(jogadorExistente -> {
                    jogador.setId(id);
                    Jogador jogadorAtualizado = jogadorService.salvar(jogador);
                    return ResponseEntity.ok(jogadorAtualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (jogadorService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        jogadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/cartola")
    public ResponseEntity<List<JogadorCartolaDTO>> listarJogadoresCartola() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.cartola.globo.com/atletas/pontuados?rodada=1";
        List<JogadorCartolaDTO> jogadores = new ArrayList<>();
        try {
            Map response = restTemplate.getForObject(url, Map.class);
            Map<String, Object> atletas = (Map<String, Object>) response.get("atletas");
            for (Object value : atletas.values()) {
                Map<String, Object> atleta = (Map<String, Object>) value;
                JogadorCartolaDTO dto = new JogadorCartolaDTO();
                dto.setId((int) atleta.get("atleta_id"));
                dto.setApelido((String) atleta.get("apelido"));
                dto.setPontuacao(atleta.get("pontuacao") != null ? ((Number) atleta.get("pontuacao")).doubleValue() : 0.0);
                dto.setPosicaoId(atleta.get("posicao_id") != null ? ((Number) atleta.get("posicao_id")).intValue() : 0);
                dto.setClubeId(atleta.get("clube_id") != null ? ((Number) atleta.get("clube_id")).intValue() : 0);
                dto.setFoto((String) atleta.get("foto"));
                jogadores.add(dto);
            }
        } catch (Exception e) {
            return ResponseEntity.status(502).body(jogadores);
        }
        return ResponseEntity.ok(jogadores);
    }

    @GetMapping("/usuarios-teste")
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosTeste() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://jsonplaceholder.typicode.com/users";
        List<UsuarioDTO> usuarios = new ArrayList<>();
        try {
            List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
            for (Map<String, Object> item : response) {
                UsuarioDTO dto = new UsuarioDTO();
                dto.setId((int) item.get("id"));
                dto.setName((String) item.get("name"));
                dto.setUsername((String) item.get("username"));
                dto.setEmail((String) item.get("email"));
                usuarios.add(dto);
            }
        } catch (Exception e) {
            return ResponseEntity.status(502).body(usuarios);
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/jogos-rodada")
    public ResponseEntity<List<JogoDTO>> listarJogosRodada() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.cartola.globo.com/partidas";
        List<JogoDTO> jogos = new ArrayList<>();
        try {
            System.out.println("[DEBUG] Chamando API externa: " + url);
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> response = responseEntity.getBody();
            System.out.println("[DEBUG] Status HTTP: " + responseEntity.getStatusCode());
            System.out.println("[DEBUG] JSON bruto recebido: " + response);
            if (response == null) {
                System.out.println("[DEBUG] Resposta nula da API externa");
                return ResponseEntity.status(502).body(jogos);
            }
            Object partidasObj = response.get("partidas");
            Object clubesObj = response.get("clubes");
            if (!(partidasObj instanceof List) || !(clubesObj instanceof Map)) {
                System.out.println("[DEBUG] Estrutura inesperada na resposta da API externa");
                return ResponseEntity.status(502).body(jogos);
            }
            List<?> partidas = (List<?>) partidasObj;
            Map<?, ?> clubes = (Map<?, ?>) clubesObj;
            for (Object partidaObj : partidas) {
                if (!(partidaObj instanceof Map)) continue;
                Map<String, Object> partida = (Map<String, Object>) partidaObj;
                JogoDTO dto = new JogoDTO();
                Object partidaIdObj = partida.get("partida_id");
                Object casaIdObj = partida.get("clube_casa_id");
                Object visitanteIdObj = partida.get("clube_visitante_id");
                Object horarioObj = partida.get("partida_data");
                if (!(partidaIdObj instanceof Number) || !(casaIdObj instanceof Number) || !(visitanteIdObj instanceof Number)) continue;
                dto.setPartida_id(((Number) partidaIdObj).intValue());
                int casaId = ((Number) casaIdObj).intValue();
                int visitanteId = ((Number) visitanteIdObj).intValue();
                Map<String, Object> casa = null;
                Map<String, Object> visitante = null;
                Object casaObj = clubes.get(String.valueOf(casaId));
                Object visitanteObj = clubes.get(String.valueOf(visitanteId));
                if (casaObj instanceof Map) casa = (Map<String, Object>) casaObj;
                if (visitanteObj instanceof Map) visitante = (Map<String, Object>) visitanteObj;
                // Clube casa
                ClubeDTO clubeCasa = new ClubeDTO();
                clubeCasa.setId(casaId);
                clubeCasa.setNome(casa != null && casa.get("nome") instanceof String ? (String) casa.get("nome") : "");
                Object escudosCasaObj = casa != null ? casa.get("escudos") : null;
                if (escudosCasaObj instanceof Map) {
                    clubeCasa.setEscudos((Map<String, String>) escudosCasaObj);
                } else {
                    clubeCasa.setEscudos(new HashMap<>());
                }
                // Clube visitante
                ClubeDTO clubeVisitante = new ClubeDTO();
                clubeVisitante.setId(visitanteId);
                clubeVisitante.setNome(visitante != null && visitante.get("nome") instanceof String ? (String) visitante.get("nome") : "");
                Object escudosVisitanteObj = visitante != null ? visitante.get("escudos") : null;
                if (escudosVisitanteObj instanceof Map) {
                    clubeVisitante.setEscudos((Map<String, String>) escudosVisitanteObj);
                } else {
                    clubeVisitante.setEscudos(new HashMap<>());
                }
                dto.setClube_casa(clubeCasa);
                dto.setClube_visitante(clubeVisitante);
                dto.setHorario(horarioObj instanceof String ? (String) horarioObj : "");
                jogos.add(dto);
            }
            System.out.println("[DEBUG] Jogos decodificados: " + jogos.size());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(502).body(jogos);
        }
        return ResponseEntity.ok(jogos);
    }
}