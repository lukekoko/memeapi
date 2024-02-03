package com.lukekoko.memeapi.reddit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukekoko.memeapi.config.AppConfig;
import com.lukekoko.memeapi.util.AccessToken;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@AllArgsConstructor
public class RedditService {
    private final AppConfig appConfig;
    private final WebClient webClient;

    private Reddit reddit;

    public CompletableFuture<String> doGetRequest(String url) {
        log.info("doing get request to {}", url);
        if (reddit.getAccessToken() == null) {
            log.info("reddit access token is null, getting new access token");
            fetchCredentials();
        }
        return webClient
                .get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(reddit.getAccessToken().getToken()))
                .header("User-agent", reddit.getUserAgent())
                .exchangeToMono(
                        response -> {
                            if (response.statusCode().equals(HttpStatus.OK)) {
                                return response.bodyToMono(String.class);
                            } else if (response.statusCode().equals(HttpStatus.UNAUTHORIZED)) {
                                log.warn("reddit request failed, getting new access token");
                                fetchCredentials();
                                return response.createException().flatMap(Mono::error);
                            } else {
                                log.error("reddit request failed: {}", response.statusCode());
                                return Mono.just(response.statusCode() + " " + url);
                            }
                        })
                .retry()
                .toFuture();
    }

    private void fetchCredentials() {
        try {
            AccessToken accessToken =
                    getAccessToken()
                            .thenApply(
                                    response -> {
                                        log.debug(response);
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        try {
                                            return objectMapper.readValue(
                                                    response, AccessToken.class);
                                        } catch (JsonProcessingException ex) {
                                            log.error("access token json processing error: ", ex);
                                            return new AccessToken();
                                        }
                                    })
                            .get();
            reddit.setAccessToken(accessToken);
        } catch (InterruptedException | ExecutionException ex) {
            log.error("access token future error: ", ex);
            Thread.currentThread().interrupt();
        }
    }

    private CompletableFuture<String> getAccessToken() {
        log.info("getting new reddit access token");
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        return webClient
                .post()
                .uri(appConfig.getUrl())
                .headers(
                        headers ->
                                headers.setBasicAuth(
                                        appConfig.getClientId(), appConfig.getClientSecret()))
                .header("User-Agent", appConfig.getUserAgent())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromValue(formData))
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
    }
}
