package com.cartola.backend.dto.api;

public enum StatusMercado {
    ABERTO(1),
    FECHADO(2);

    private final int valor;

    StatusMercado(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static StatusMercado fromValor(int valor) {
        for (StatusMercado status : values()) {
            if (status.getValor() == valor) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + valor);
    }
}