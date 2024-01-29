package com.lukekoko.memeapi.config;

import com.lukekoko.memeapi.reddit.Reddit;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "reddit")
@EnableCaching
public class AppConfig {

    private String clientId;

    private String clientSecret;

    private String userAgent;

    private String url;

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
                .build();
    }

    @Bean
    public Reddit reddit() {
        return new Reddit(null, clientId, clientSecret, userAgent);
    }
}
