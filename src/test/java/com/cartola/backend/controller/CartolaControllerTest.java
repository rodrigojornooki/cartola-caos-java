package com.cartola.backend.controller;

import com.cartola.backend.dto.api.CartolaApiResponse;
import com.cartola.backend.dto.api.StatusMercado;
import com.cartola.backend.model.ScoutNegativo;
import com.cartola.backend.service.integration.CartolaApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
public class CartolaControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CartolaApiService cartolaApiService;

    @Test
    void testGetPontuacoesAtuais() {
        // Preparar dados de teste
        List<ScoutNegativo> mockScouts = Arrays.asList(
            createMockScout(1L, -3.5),
            createMockScout(2L, -5.0)
        );

        // Configurar comportamento do mock
        when(cartolaApiService.buscarPontuacoesAtual()).thenReturn(mockScouts);

        // Executar teste
        webTestClient.get()
            .uri("/api/cartola/pontuacoes")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ScoutNegativo.class)
            .hasSize(2)
            .contains(mockScouts.toArray(new ScoutNegativo[0]));
    }

    @Test
    void testGetStatusMercado() {
        // Preparar dados de teste
        CartolaApiResponse mockResponse = new CartolaApiResponse();
        mockResponse.setStatus_mercado(StatusMercado.ABERTO);

        // Configurar comportamento do mock
        when(cartolaApiService.buscarMercado()).thenReturn(mockResponse);

        // Executar teste
        webTestClient.get()
            .uri("/api/cartola/mercado/status")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void testGetHistoricoJogador() {
        // Preparar dados de teste
        Long jogadorId = 1L;
        List<ScoutNegativo> mockHistorico = Arrays.asList(
            createMockScout(jogadorId, -2.0),
            createMockScout(jogadorId, -4.0)
        );

        // Configurar comportamento do mock
        when(cartolaApiService.buscarHistoricoJogador(any(Long.class))).thenReturn(mockHistorico);

        // Executar teste
        webTestClient.get()
            .uri("/api/cartola/jogadores/" + jogadorId + "/historico")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ScoutNegativo.class)
            .hasSize(2)
            .contains(mockHistorico.toArray(new ScoutNegativo[0]));
    }

    private ScoutNegativo createMockScout(Long atletaId, Double pontuacao) {
        ScoutNegativo scout = new ScoutNegativo();
        scout.setAtletaId(atletaId);
        scout.setPontuacaoTotal(pontuacao);
        return scout;
    }
}