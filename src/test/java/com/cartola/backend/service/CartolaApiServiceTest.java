package com.cartola.backend.service;

import com.cartola.backend.dto.api.AtletaDTO;
import com.cartola.backend.model.ScoutNegativo;
import com.cartola.backend.service.integration.CartolaApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartolaApiServiceTest {

    @Autowired
    private CartolaApiService cartolaApiService;

    @Test
    void testProcessarScouts() {
        // Criar um atleta com scouts negativos
        AtletaDTO atleta = new AtletaDTO();
        atleta.setAtleta_id(1L);

        atleta.setScout(Map.of(
            "GC", 1,
            "CV", 1,
            "CA", 1,
            "PP", 1
        ));

        // Processar os scouts
        ScoutNegativo resultado = cartolaApiService.processarScouts(atleta);

        // Verificações
        assertNotNull(resultado);
        assertEquals(1L, resultado.getAtletaId());
        assertEquals(1, resultado.getGolsContra());
        assertEquals(1, resultado.getCartaoVermelho());
        assertEquals(1, resultado.getCartaoAmarelo());
        assertEquals(1, resultado.getPenaltiPerdido());
        
        // A pontuação total deve ser negativa
        assertNotNull(resultado.getPontuacaoTotal());
        assertTrue(resultado.getPontuacaoTotal() < 0);
    }

    @Test
    void testProcessarScouts_SemScouts() {
        // Criar um atleta sem scouts
        AtletaDTO atleta = new AtletaDTO();
        atleta.setAtleta_id(1L);

        // Processar os scouts
        ScoutNegativo resultado = cartolaApiService.processarScouts(atleta);

        // Verificações
        assertNotNull(resultado);
        assertEquals(1L, resultado.getAtletaId());
        assertEquals(0, resultado.getGolsContra());
        assertEquals(0, resultado.getCartaoVermelho());
        assertEquals(0.0, resultado.getPontuacaoTotal());
    }
}