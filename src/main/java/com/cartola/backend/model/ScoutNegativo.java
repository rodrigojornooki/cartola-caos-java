package com.cartola.backend.model;

import jakarta.persistence.*;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "scouts_negativos")
public class ScoutNegativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jogador_id")
    private Jogador jogador;

    private Integer golsContra; // GC
    private Integer cartaoVermelho; // CV
    private Integer cartaoAmarelo; // CA
    private Integer penaltiPerdido; // PP
    private Integer golsPerdidos; // FT (Finalizações para fora)
    private Integer impedimentos; // I
    private Integer faltas; // FC
    private Integer passes_errados; // PE
    private Integer golsSofridos; // GS
    private Integer finalizacaoTrave; // FT
    private Double pontuacaoTotal;

    private Long atletaId;

    // Pesos para cada scout negativo
    public static final Map<String, Double> PESOS_SCOUTS = new HashMap<>() {{
        put("GC", 10.0);  // Gol contra vale 10 pontos positivos!
        put("CV", 8.0);   // Expulsão vale 8 pontos positivos
        put("PP", 7.0);   // Pênalti perdido vale 7 pontos positivos
        put("CA", 3.0);   // Cartão amarelo vale 3 pontos positivos
        put("FC", 2.0);   // Falta cometida vale 2 pontos positivos
        put("FT", 1.5);   // Finalização na trave vale 1.5 pontos (quase gol contra!)
        put("I", 1.0);    // Impedimento vale 1 ponto positivo
        put("GS", 0.5);   // Gol sofrido (para goleiros) vale 0.5 ponto positivo
    }};

    public void calcularPontuacaoTotal() {
        this.pontuacaoTotal = 0.0;
        
        // Soma todos os scouts negativos multiplicados pelos seus pesos
        if (golsContra != null) this.pontuacaoTotal += golsContra * PESOS_SCOUTS.get("GC");
        if (cartaoVermelho != null) this.pontuacaoTotal += cartaoVermelho * PESOS_SCOUTS.get("CV");
        if (cartaoAmarelo != null) this.pontuacaoTotal += cartaoAmarelo * PESOS_SCOUTS.get("CA");
        if (penaltiPerdido != null) this.pontuacaoTotal += penaltiPerdido * PESOS_SCOUTS.get("PP");
        if (finalizacaoTrave != null) this.pontuacaoTotal += finalizacaoTrave * PESOS_SCOUTS.get("FT");
        if (impedimentos != null) this.pontuacaoTotal += impedimentos * PESOS_SCOUTS.get("I");
        if (faltas != null) this.pontuacaoTotal += faltas * PESOS_SCOUTS.get("FC");
        if (golsSofridos != null) this.pontuacaoTotal += golsSofridos * PESOS_SCOUTS.get("GS");
        if (passes_errados != null) this.pontuacaoTotal += passes_errados * PESOS_SCOUTS.get("PE");
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Integer getGolsContra() {
        return golsContra;
    }

    public void setGolsContra(Integer golsContra) {
        this.golsContra = golsContra;
    }

    public Integer getCartaoVermelho() {
        return cartaoVermelho;
    }

    public void setCartaoVermelho(Integer cartaoVermelho) {
        this.cartaoVermelho = cartaoVermelho;
    }

    public Integer getCartaoAmarelo() {
        return cartaoAmarelo;
    }

    public void setCartaoAmarelo(Integer cartaoAmarelo) {
        this.cartaoAmarelo = cartaoAmarelo;
    }

    public Integer getPenaltiPerdido() {
        return penaltiPerdido;
    }

    public void setPenaltiPerdido(Integer penaltiPerdido) {
        this.penaltiPerdido = penaltiPerdido;
    }

    public Integer getGolsPerdidos() {
        return golsPerdidos;
    }

    public void setGolsPerdidos(Integer golsPerdidos) {
        this.golsPerdidos = golsPerdidos;
    }

    public Integer getImpedimentos() {
        return impedimentos;
    }

    public void setImpedimentos(Integer impedimentos) {
        this.impedimentos = impedimentos;
    }

    public Integer getFaltas() {
        return faltas;
    }

    public void setFaltas(Integer faltas) {
        this.faltas = faltas;
    }

    public Integer getPasses_errados() {
        return passes_errados;
    }

    public void setPasses_errados(Integer passes_errados) {
        this.passes_errados = passes_errados;
    }

    public Integer getGolsSofridos() {
        return golsSofridos;
    }

    public void setGolsSofridos(Integer golsSofridos) {
        this.golsSofridos = golsSofridos;
    }

    public Integer getFinalizacaoTrave() {
        return finalizacaoTrave;
    }

    public void setFinalizacaoTrave(Integer finalizacaoTrave) {
        this.finalizacaoTrave = finalizacaoTrave;
    }

    public Long getAtletaId() {
        return atletaId;
    }

    public void setAtletaId(Long atletaId) {
        this.atletaId = atletaId;
    }

    public Double getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public void setPontuacaoTotal(Double pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }
}