package com.cartola.backend.dto.api;

import java.util.Map;

public class AtletaDTO {
    private Long atleta_id;
    private String apelido;
    private String posicao_id;
    private String clube_id;
    private Double pontuacao;
    private Map<String, Object> scout;

    public Long getAtleta_id() {
        return atleta_id;
    }

    public void setAtleta_id(Long atleta_id) {
        this.atleta_id = atleta_id;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getPosicao_id() {
        return posicao_id;
    }

    public void setPosicao_id(String posicao_id) {
        this.posicao_id = posicao_id;
    }

    public String getClube_id() {
        return clube_id;
    }

    public void setClube_id(String clube_id) {
        this.clube_id = clube_id;
    }

    public Double getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Double pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Map<String, Object> getScout() {
        return scout;
    }

    public void setScout(Map<String, Object> scout) {
        this.scout = scout;
    }
}