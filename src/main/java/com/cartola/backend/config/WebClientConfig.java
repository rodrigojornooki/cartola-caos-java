package com.cartola.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.cache.annotation.EnableCaching;

@Configuration
@EnableCaching
public class WebClientConfig {
    
    @Value("${cartola.token}")
    private String cartolaToken;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.cartola.globo.com")
                .defaultHeader("Accept", "*/*")
                .defaultHeader("Accept-Language", "pt-BR,pt;q=0.9")
                .defaultHeader("Authorization", "Bearer " + cartolaToken)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}