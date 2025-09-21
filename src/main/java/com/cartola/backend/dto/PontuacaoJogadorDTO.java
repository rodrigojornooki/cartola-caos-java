package com.cartola.backend.dto;

import java.util.Map;

public class PontuacaoJogadorDTO {
    private Long atleta_id;
    private String apelido;
    private Double pontuacao;
    private String posicao;
    private String clube;
    private Map<String, Object> scouts;

    // Getters e Setters
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

    public Double getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Double pontuacao) {
        this.pontuacao = pontuacao;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public String getClube() {
        return clube;
    }

    public void setClube(String clube) {
        this.clube = clube;
    }

    public Map<String, Object> getScouts() {
        return scouts;
    }

    public void setScouts(Map<String, Object> scouts) {
        this.scouts = scouts;
    }
}