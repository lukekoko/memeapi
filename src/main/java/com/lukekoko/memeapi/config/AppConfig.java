package com.lukekoko.memeapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "reddit")
public class AppConfig {

    private String clientId;

    private String clientSecret;

    private String userAgent;

    private String url;

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }
}
