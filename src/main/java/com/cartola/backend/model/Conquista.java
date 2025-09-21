package com.cartola.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "conquistas")
public class Conquista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    private Integer pontosBonificacao;

    // Tipos de conquistas disponíveis
    public static final String EXPULSAO_RAPIDA = "Velocista do Cartão";
    public static final String HAT_TRICK_CONTRA = "Hat-trick às Avessas";
    public static final String PENALTI_MASTER = "Mestre dos Pênaltis Perdidos";
    public static final String ZAGUEIRO_ARTILHEIRO = "Artilheiro do Próprio Gol";
    public static final String IMPEDIMENTO_EXPERT = "PhD em Impedimentos";
    public static final String FALTA_MASTER = "Especialista em Faltas";
    public static final String PASSE_ERRADO_PRO = "Rei do Passe Errado";

    // Critérios para conquistas
    public static boolean verificarExpulsaoRapida(ScoutNegativo scout) {
        return scout.getCartaoVermelho() > 0 && scout.getFaltas() <= 2;
    }

    public static boolean verificarHatTrickContra(ScoutNegativo scout) {
        return scout.getGolsContra() >= 3;
    }

    public static boolean verificarPenaltiMaster(ScoutNegativo scout) {
        return scout.getPenaltiPerdido() >= 2;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getPontosBonificacao() {
        return pontosBonificacao;
    }

    public void setPontosBonificacao(Integer pontosBonificacao) {
        this.pontosBonificacao = pontosBonificacao;
    }
}