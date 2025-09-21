package com.cartola.backend.service;

import com.cartola.backend.model.Conquista;
import com.cartola.backend.model.ScoutNegativo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConquistaService {

    public List<Conquista> verificarConquistas(ScoutNegativo scout) {
        List<Conquista> conquistasObtidas = new ArrayList<>();

        // Verifica expulsão rápida
        if (Conquista.verificarExpulsaoRapida(scout)) {
            Conquista conquista = new Conquista();
            conquista.setTitulo(Conquista.EXPULSAO_RAPIDA);
            conquista.setDescricao("Conseguiu ser expulso com menos de 2 faltas. Impressionante!");
            conquista.setPontosBonificacao(15);
            conquistasObtidas.add(conquista);
        }

        // Verifica hat-trick de gols contra
        if (Conquista.verificarHatTrickContra(scout)) {
            Conquista conquista = new Conquista();
            conquista.setTitulo(Conquista.HAT_TRICK_CONTRA);
            conquista.setDescricao("3 gols contra no mesmo jogo. Artilheiro nato... do próprio gol!");
            conquista.setPontosBonificacao(30);
            conquistasObtidas.add(conquista);
        }

        // Verifica mestre dos pênaltis perdidos
        if (Conquista.verificarPenaltiMaster(scout)) {
            Conquista conquista = new Conquista();
            conquista.setTitulo(Conquista.PENALTI_MASTER);
            conquista.setDescricao("Perdeu 2 ou mais pênaltis. A trave é sua melhor amiga!");
            conquista.setPontosBonificacao(20);
            conquistasObtidas.add(conquista);
        }

        // Adiciona bônus específicos por posição
        if (scout.getFaltas() >= 5) {
            Conquista conquista = new Conquista();
            conquista.setTitulo(Conquista.FALTA_MASTER);
            conquista.setDescricao("5+ faltas em um jogo. Quem precisa da bola quando se tem as pernas do adversário?");
            conquista.setPontosBonificacao(10);
            conquistasObtidas.add(conquista);
        }

        if (scout.getPasses_errados() >= 20) {
            Conquista conquista = new Conquista();
            conquista.setTitulo(Conquista.PASSE_ERRADO_PRO);
            conquista.setDescricao("20+ passes errados. Sua precisão é impressionante... para o outro time!");
            conquista.setPontosBonificacao(12);
            conquistasObtidas.add(conquista);
        }

        return conquistasObtidas;
    }

    public double calcularBonusConquistas(List<Conquista> conquistas) {
        return conquistas.stream()
                .mapToDouble(c -> c.getPontosBonificacao())
                .sum();
    }
}