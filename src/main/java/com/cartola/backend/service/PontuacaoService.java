package com.cartola.backend.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PontuacaoService {

    private final Map<String, Double> regrasPersonalizadas;

    public PontuacaoService() {
        this.regrasPersonalizadas = inicializarRegras();
    }

    /**
     * Calcula a pontuação personalizada baseada no scout do jogador
     * @param scout Mapa com as estatísticas do jogador
     * @return Pontuação calculada
     */
    public double calcularPontuacaoPersonalizada(Map<String, Object> scout) {
        if (scout == null || scout.isEmpty()) {
            return 0.0;
        }

        double pontuacao = 0.0;

        for (Map.Entry<String, Object> entry : scout.entrySet()) {
            String sigla = entry.getKey();
            Object valorObj = entry.getValue();

            if (regrasPersonalizadas.containsKey(sigla) && valorObj instanceof Number) {
                double valor = ((Number) valorObj).doubleValue();
                double multiplicador = regrasPersonalizadas.get(sigla);
                pontuacao += valor * multiplicador;
            }
        }

        return Math.round(pontuacao * 100.0) / 100.0; // Arredonda para 2 casas decimais
    }

    /**
     * Inicializa as regras de pontuação personalizada
     * @return Mapa com as regras de pontuação
     */
    private Map<String, Double> inicializarRegras() {
        Map<String, Double> regras = new HashMap<>();
        
        // Ações positivas
        regras.put("G", 8.0);     // Gol
        regras.put("A", 5.0);     // Assistência
        regras.put("SG", 5.0);    // Saldo de gols (para goleiros)
        regras.put("DS", 1.0);    // Desarme
        regras.put("FS", 0.5);    // Falta sofrida
        regras.put("PE", 0.1);    // Passe errado (penalização leve)
        
        // Ações negativas
        regras.put("FC", -0.3);   // Falta cometida
        regras.put("GC", -2.0);   // Gol contra
        regras.put("CV", -5.0);   // Cartão vermelho
        regras.put("CA", -1.0);   // Cartão amarelo
        regras.put("PP", -3.0);   // Pênalti perdido
        regras.put("GS", -2.0);   // Gol sofrido (para goleiros)
        
        return regras;
    }

    /**
     * Retorna as regras de pontuação atuais
     * @return Cópia das regras de pontuação
     */
    public Map<String, Double> obterRegras() {
        return new HashMap<>(regrasPersonalizadas);
    }

    /**
     * Atualiza uma regra de pontuação específica
     * @param sigla Sigla da estatística
     * @param valor Novo valor para a regra
     */
    public void atualizarRegra(String sigla, Double valor) {
        if (sigla != null && valor != null) {
            regrasPersonalizadas.put(sigla, valor);
        }
    }

    /**
     * Remove uma regra de pontuação
     * @param sigla Sigla da estatística a ser removida
     */
    public void removerRegra(String sigla) {
        regrasPersonalizadas.remove(sigla);
    }

    /**
     * Adiciona uma nova regra de pontuação
     * @param sigla Sigla da nova estatística
     * @param valor Valor da regra
     */
    public void adicionarRegra(String sigla, Double valor) {
        if (sigla != null && valor != null) {
            regrasPersonalizadas.put(sigla, valor);
        }
    }
}