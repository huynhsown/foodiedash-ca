package com.ute.foodiedash.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnProperty(prefix = "openrouteservice.api", name = "key")
public class OpenRouteConfig {
    @Value("${openrouteservice.base-url}")
    private String baseUrl;

    @Bean
    public WebClient openRouteServiceWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
