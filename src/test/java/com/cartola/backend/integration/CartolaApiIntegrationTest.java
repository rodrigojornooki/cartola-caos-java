package com.cartola.backend.integration;

import com.cartola.backend.dto.api.CartolaApiResponse;
import com.cartola.backend.service.integration.CartolaApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class CartolaApiIntegrationTest {

    @Autowired
    private CartolaApiService cartolaApiService;

    @Test
    void testBuscarMercado() {
        CartolaApiResponse response = cartolaApiService.buscarMercado();
        // O teste falhará se a API estiver indisponível, que é um comportamento esperado
        // em testes de integração
        assertNotNull(response);
    }

    @Test
    void testBuscarPontuacoesAtual() {
        var scouts = cartolaApiService.buscarPontuacoesAtual();
        // Mesmo que não haja jogos em andamento, a lista não deve ser nula
        assertNotNull(scouts);
    }
}