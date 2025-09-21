package com.cartola.backend.dto;

import java.util.Map;

public class ClubeDTO {
    private int id;
    private String nome;
    private Map<String, String> escudos;

    public ClubeDTO() {}

    public ClubeDTO(int id, String nome, Map<String, String> escudos) {
        this.id = id;
        this.nome = nome;
        this.escudos = escudos;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Map<String, String> getEscudos() { return escudos; }
    public void setEscudos(Map<String, String> escudos) { this.escudos = escudos; }
}

