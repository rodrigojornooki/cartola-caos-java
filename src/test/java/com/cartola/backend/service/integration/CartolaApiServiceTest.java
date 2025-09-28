package com.cartola.backend.service.integration;

import com.cartola.backend.dto.api.CartolaApiResponse;
import com.cartola.backend.dto.api.AtletaDTO;
import com.cartola.backend.dto.PontuacaoJogadorDTO;
import com.cartola.backend.model.ScoutNegativo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CartolaApiServiceTest {

    @Autowired
    private CartolaApiIntegrationService cartolaApiService;

    @MockBean
    private WebClient webClient;

    private WebClient.ResponseSpec responseSpec;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        // Configurar mocks
        var requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        // Configurar comportamento do WebClient
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void testBuscarMercado_Sucesso() {
        // Preparar dados de teste
        CartolaApiResponse mockResponse = new CartolaApiResponse();
        Map<String, AtletaDTO> atletas = new HashMap<>();
        
        AtletaDTO atleta = new AtletaDTO();
        atleta.setAtleta_id(1L);
        atleta.setApelido("Teste");

        Map<String, Object> scouts = new HashMap<>();
        scouts.put("GC", 1.0);
        scouts.put("CV", 1.0);
        atleta.setScout(scouts);
        
        atletas.put("1", atleta);
        mockResponse.setAtletas(atletas);

        // Configurar mock para retornar os dados de teste
        when(responseSpec.bodyToMono(CartolaApiResponse.class)).thenReturn(Mono.just(mockResponse));

        // Executar o teste
        CartolaApiResponse resultado = cartolaApiService.buscarMercado();

        // Verificar resultados
        assertNotNull(resultado);
        assertNotNull(resultado.getAtletas());
        assertEquals(1, resultado.getAtletas().size());
        
        AtletaDTO atletaResultado = resultado.getAtletas().get("1");
        assertNotNull(atletaResultado);
        assertEquals(1L, atletaResultado.getAtleta_id());
        assertEquals("Teste", atletaResultado.getApelido());
        assertEquals(1.0, ((Number)atletaResultado.getScout().get("GC")).doubleValue());
        assertEquals(1.0, ((Number)atletaResultado.getScout().get("CV")).doubleValue());
    }

    @Test
    void testProcessarScouts_ComScoutsNegativos() {
        // Preparar dados de teste
        AtletaDTO atleta = new AtletaDTO();

        Map<String, Object> scouts = new HashMap<>();
        scouts.put("GC", 1.0);
        scouts.put("CV", 1.0);
        scouts.put("CA", 1.0);
        scouts.put("FC", 2.0);
        atleta.setScout(scouts);

        // Executar o teste
        ScoutNegativo resultado = cartolaApiService.processarScouts(atleta);

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(1, resultado.getGolsContra());
        assertEquals(1, resultado.getCartaoVermelho());
        assertEquals(1, resultado.getCartaoAmarelo());
        assertEquals(2, resultado.getFaltas());
        assertTrue(resultado.getPontuacaoTotal() < 0);
    }

    @Test
    void testBuscarMercado_ErroApi() {
        // Simular erro na API
        when(responseSpec.bodyToMono(CartolaApiResponse.class))
            .thenReturn(Mono.error(new RuntimeException("Erro de API")));

        // Executar o teste
        CartolaApiResponse resultado = cartolaApiService.buscarMercado();

        // Verificar que o método retorna null em caso de erro
        assertNull(resultado);
    }

    @Test
    void testBuscarPontuacoesRodada() {
        // Preparar dados de teste
        CartolaApiResponse mockResponse = new CartolaApiResponse();
        Map<String, AtletaDTO> atletas = new HashMap<>();

        // Atleta com scouts negativos diversos
        AtletaDTO atleta1 = new AtletaDTO();
        atleta1.setAtleta_id(1L);
        atleta1.setApelido("Jogador 1");
        atleta1.setPosicao_id("GOL");
        atleta1.setPontuacao(-2.0);

        Map<String, Object> scouts1 = new HashMap<>();
        scouts1.put("GS", 2.0);  // 2 gols sofridos
        scouts1.put("CV", 1.0);  // 1 cartão vermelho
        scouts1.put("CA", 1.0);  // 1 cartão amarelo
        atleta1.setScout(scouts1);

        // Atleta com scout positivo (para testar o contraste)
        AtletaDTO atleta2 = new AtletaDTO();
        atleta2.setAtleta_id(2L);
        atleta2.setApelido("Jogador 2");
        atleta2.setPosicao_id("ATA");
        atleta2.setPontuacao(5.0);

        Map<String, Object> scouts2 = new HashMap<>();
        scouts2.put("G", 1.0);   // 1 gol
        scouts2.put("FD", 2.0);  // 2 finalizações defendidas
        atleta2.setScout(scouts2);

        atletas.put("1", atleta1);
        atletas.put("2", atleta2);
        mockResponse.setAtletas(atletas);

        // Configurar mock para retornar os dados de teste
        when(responseSpec.bodyToMono(CartolaApiResponse.class)).thenReturn(Mono.just(mockResponse));

        // Executar o teste
        List<PontuacaoJogadorDTO> resultado = cartolaApiService.buscarPontuacoesRodada(1);

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        // Verificar jogador com scouts negativos
        PontuacaoJogadorDTO jogador1 = resultado.stream()
            .filter(j -> j.getAtleta_id().equals(1L))
            .findFirst()
            .orElse(null);

        assertNotNull(jogador1);
        assertEquals("Jogador 1", jogador1.getApelido());
        assertEquals("GOL", jogador1.getPosicao());
        assertEquals(-2.0, jogador1.getPontuacao());

        Map<String, Object> scoutsJogador1 = jogador1.getScouts();
        assertEquals(2.0, ((Number)scoutsJogador1.get("GS")).doubleValue());
        assertEquals(1.0, ((Number)scoutsJogador1.get("CV")).doubleValue());
        assertEquals(1.0, ((Number)scoutsJogador1.get("CA")).doubleValue());
    }

    @Test
    void testProcessarScouts_ComDiversosScouts() {
        // Preparar dados de teste
        AtletaDTO atleta = new AtletaDTO();
        atleta.setAtleta_id(1L);

        Map<String, Object> scouts = new HashMap<>();
        scouts.put("GC", 1.0);  // Gol contra
        scouts.put("CV", 1.0);  // Cartão vermelho
        scouts.put("CA", 2.0);  // Dois cartões amarelos
        scouts.put("FC", 3.0);  // Três faltas cometidas
        scouts.put("I", 2.0);   // Dois impedimentos
        scouts.put("PP", 1.0);  // Um pênalti perdido
        scouts.put("GS", 2.0);  // Dois gols sofridos (goleiro)
        scouts.put("FT", 1.0);  // Uma finalização na trave
        atleta.setScout(scouts);

        // Executar o teste
        ScoutNegativo resultado = cartolaApiService.processarScouts(atleta);

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(1, resultado.getGolsContra());
        assertEquals(1, resultado.getCartaoVermelho());
        assertEquals(2, resultado.getCartaoAmarelo());
        assertEquals(3, resultado.getFaltas());
        assertEquals(2, resultado.getImpedimentos());
        assertEquals(1, resultado.getPenaltiPerdido());
        assertEquals(2, resultado.getGolsSofridos());
        assertEquals(1, resultado.getFinalizacaoTrave());

        // Verificar se a pontuação total é calculada corretamente
        assertNotNull(resultado.getPontuacaoTotal());
        double pontuacaoEsperada =
            1 * 10.0 +  // GC = 10 pontos
            1 * 8.0 +   // CV = 8 pontos
            2 * 3.0 +   // CA = 3 pontos cada
            3 * 2.0 +   // FC = 2 pontos cada
            2 * 1.0 +   // I = 1 ponto cada
            1 * 7.0 +   // PP = 7 pontos
            2 * 0.5 +   // GS = 0.5 pontos cada
            1 * 1.5;    // FT = 1.5 pontos

        assertEquals(pontuacaoEsperada, resultado.getPontuacaoTotal());
    }
}

