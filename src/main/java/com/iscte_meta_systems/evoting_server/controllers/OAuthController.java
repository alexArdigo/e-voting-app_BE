package com.iscte_meta_systems.evoting_server.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.view.RedirectView;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    WebClient webClient;

    public OAuthController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build(); // build once and reuse
    }

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${chave.movel.base-url}")
    private String chavemovelUrl;

    @Value("${oauth.client.id}")
    private String clientId;

    @Value("${oauth.client.secret}")
    private String clientSecret;

    @Value("${oauth.redirect.uri}")
    private String redirectUri;

    @GetMapping("/login")
    public RedirectView login() {
        String authUrl = "http://localhost:9090/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri;

        return new RedirectView(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        System.out.println("code-callback = " + code);

        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        return webClient.post()
                .uri(chavemovelUrl + "/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("code", code)
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirectUri))
                .retrieve()
                .bodyToMono(String.class)
                .map(token -> ResponseEntity.ok("Token: " + token))
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error fetching token: " + e.getMessage()));
                })
                .block();
    }
}
