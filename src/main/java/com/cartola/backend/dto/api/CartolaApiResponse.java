package com.cartola.backend.dto.api;

import java.util.Map;

public class CartolaApiResponse {
    private Map<String, AtletaDTO> atletas;
    private String rodada;
    private StatusMercado status_mercado;

    public Map<String, AtletaDTO> getAtletas() {
        return atletas;
    }

    public void setAtletas(Map<String, AtletaDTO> atletas) {
        this.atletas = atletas;
    }

    public String getRodada() {
        return rodada;
    }

    public void setRodada(String rodada) {
        this.rodada = rodada;
    }

    public StatusMercado getStatus_mercado() {
        return status_mercado;
    }

    public void setStatus_mercado(StatusMercado status_mercado) {
        this.status_mercado = status_mercado;
    }
}