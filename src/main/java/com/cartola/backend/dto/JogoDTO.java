package com.cartola.backend.dto;

public class JogoDTO {
    private int partida_id;
    private ClubeDTO clube_casa;
    private ClubeDTO clube_visitante;
    private String horario;

    public JogoDTO() {}

    public JogoDTO(int partida_id, ClubeDTO clube_casa, ClubeDTO clube_visitante, String horario) {
        this.partida_id = partida_id;
        this.clube_casa = clube_casa;
        this.clube_visitante = clube_visitante;
        this.horario = horario;
    }

    public int getPartida_id() { return partida_id; }
    public void setPartida_id(int partida_id) { this.partida_id = partida_id; }

    public ClubeDTO getClube_casa() { return clube_casa; }
    public void setClube_casa(ClubeDTO clube_casa) { this.clube_casa = clube_casa; }

    public ClubeDTO getClube_visitante() { return clube_visitante; }
    public void setClube_visitante(ClubeDTO clube_visitante) { this.clube_visitante = clube_visitante; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
}
