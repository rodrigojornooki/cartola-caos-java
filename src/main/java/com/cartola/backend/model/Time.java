package com.cartola.backend.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "times")
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String nomeUsuario;

    private Double patrimonio;

    @OneToMany(mappedBy = "time", cascade = CascadeType.ALL)
    private Set<Jogador> jogadores = new HashSet<>();

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public Double getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(Double patrimonio) {
        this.patrimonio = patrimonio;
    }

    public Set<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(Set<Jogador> jogadores) {
        this.jogadores = jogadores;
    }
}