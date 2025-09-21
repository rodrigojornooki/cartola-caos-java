package com.cartola.backend.dto;


public class JogadorCartolaDTO {
    private int id;
    private String apelido;
    private double pontuacao;
    private int posicaoId;
    private int clubeId;
    private String foto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public double getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(double pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getPosicaoId() {
        return posicaoId;
    }

    public void setPosicaoId(int posicaoId) {
        this.posicaoId = posicaoId;
    }

    public int getClubeId() {
        return clubeId;
    }

    public void setClubeId(int clubeId) {
        this.clubeId = clubeId;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
