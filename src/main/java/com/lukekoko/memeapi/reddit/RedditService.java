package com.lukekoko.memeapi.reddit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukekoko.memeapi.config.AppConfig;
import com.lukekoko.memeapi.util.AccessToken;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@AllArgsConstructor
public class RedditService {
    private final AppConfig appConfig;
    private final WebClient webClient;

    private static Reddit reddit;

    @PostConstruct
    private void init() throws JsonProcessingException {
        String clientId = appConfig.getClientId();
        String clientSecret = appConfig.getClientSecret();
        String userAgent = appConfig.getUserAgent();
        AccessToken accessToken = getAccessToken(clientId, clientSecret, userAgent);
        reddit = new Reddit(accessToken, clientId, clientSecret, userAgent);
    }

    public AccessToken getAccessToken(String clientId, String clientSecret, String userAgent)
            throws JsonProcessingException {
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");

        WebClient client = WebClient.create();
        String response =
                client.post()
                        .uri(appConfig.getUrl())
                        .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
                        .header("User-Agent", userAgent)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(BodyInserters.fromValue(formData))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
        log.info(response);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, AccessToken.class);
    }

    public String doGetRequest(String url) {
        return webClient
                .get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(reddit.getAccessToken().getToken()))
                .header("User-agent", reddit.getUserAgent())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
