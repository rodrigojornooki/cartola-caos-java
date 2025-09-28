package com.cartola.backend.service;

import com.cartola.backend.dto.UsuarioDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ExternalApiService {

    private static final Logger logger = Logger.getLogger(ExternalApiService.class.getName());
    
    private final RestTemplate restTemplate;

    public ExternalApiService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Busca usuários de teste da API JSONPlaceholder
     * @return Lista de usuários
     */
    @SuppressWarnings("unchecked")
    public List<UsuarioDTO> buscarUsuariosTeste() {
        String url = "https://jsonplaceholder.typicode.com/users";
        List<UsuarioDTO> usuarios = new ArrayList<>();
        
        try {
            logger.info("Buscando usuários de teste da API: " + url);
            
            List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
            
            if (response == null) {
                logger.warning("Resposta nula da API de usuários teste");
                return usuarios;
            }
            
            for (Map<String, Object> item : response) {
                UsuarioDTO dto = mapearUsuario(item);
                usuarios.add(dto);
            }
            
            logger.info("Busca de usuários teste concluída: " + usuarios.size() + " usuários encontrados");
        } catch (Exception e) {
            logger.severe("Erro ao buscar usuários teste: " + e.getMessage());
            throw new RuntimeException("Falha ao buscar usuários teste", e);
        }
        
        return usuarios;
    }

    /**
     * Mapeia um objeto usuário da API para UsuarioDTO
     * @param item Dados do usuário da API
     * @return UsuarioDTO mapeado
     */
    private UsuarioDTO mapearUsuario(Map<String, Object> item) {
        UsuarioDTO dto = new UsuarioDTO();
        
        // Conversão segura dos tipos
        Object idObj = item.get("id");
        if (idObj instanceof Number) {
            dto.setId(((Number) idObj).intValue());
        }
        
        Object nameObj = item.get("name");
        if (nameObj instanceof String) {
            dto.setName((String) nameObj);
        }
        
        Object usernameObj = item.get("username");
        if (usernameObj instanceof String) {
            dto.setUsername((String) usernameObj);
        }
        
        Object emailObj = item.get("email");
        if (emailObj instanceof String) {
            dto.setEmail((String) emailObj);
        }
        
        return dto;
    }

    /**
     * Método genérico para fazer chamadas GET para APIs externas
     * @param url URL da API
     * @param responseType Classe do tipo de resposta esperado
     * @param <T> Tipo de retorno
     * @return Resposta da API
     */
    public <T> T fazerChamadaGet(String url, Class<T> responseType) {
        try {
            logger.info("Fazendo chamada GET para: " + url);
            return restTemplate.getForObject(url, responseType);
        } catch (Exception e) {
            logger.severe("Erro na chamada GET para " + url + ": " + e.getMessage());
            throw new RuntimeException("Falha na chamada para API externa: " + url, e);
        }
    }

    /**
     * Método genérico para fazer chamadas POST para APIs externas
     * @param url URL da API
     * @param requestBody Corpo da requisição
     * @param responseType Classe do tipo de resposta esperado
     * @param <T> Tipo de retorno
     * @return Resposta da API
     */
    public <T> T fazerChamadaPost(String url, Object requestBody, Class<T> responseType) {
        try {
            logger.info("Fazendo chamada POST para: " + url);
            return restTemplate.postForObject(url, requestBody, responseType);
        } catch (Exception e) {
            logger.severe("Erro na chamada POST para " + url + ": " + e.getMessage());
            throw new RuntimeException("Falha na chamada POST para API externa: " + url, e);
        }
    }
}