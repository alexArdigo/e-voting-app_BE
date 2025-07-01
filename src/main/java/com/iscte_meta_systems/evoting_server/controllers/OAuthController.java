package com.iscte_meta_systems.evoting_server.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        this.webClient = webClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build(); // build once and reuse
    }

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private VoterController voterController;

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
        System.out.println(" Login initiated");
        String authUrl = chavemovelUrl + "/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri;

        return new RedirectView(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code, HttpSession session) {
        String token = webClient.post()
                .uri(chavemovelUrl + "/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("code", code)
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirectUri))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Failed to retrieve access token");
        }

        String accessToken = getAccessToken(token);

        session.setAttribute("access_token", accessToken);

        String redirectUrl = "http://localhost:5173/voter-data";

        return ResponseEntity.ok().body(Map.of("redirect", redirectUrl));
    }

    public String getAccessToken(String token) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode tokenJson = objectMapper.readTree(token);

            // Extract user info
            JsonNode userInfo = tokenJson.get("user");

            addVoter(userInfo);

            // Extract access token
            String accessToken = tokenJson.get("access_token").get("token").asText();

            return accessToken;

        } catch (Exception e) {
            System.err.println("Error parsing token response: " + e.getMessage());
            throw new RuntimeException("Failed to parse token response", e);
        }
    }

    public void addVoter(JsonNode userInfo) {
        VoterDTO voterDTO = new VoterDTO(userInfo);
        voterController.saveVoterAuthenticated(voterDTO);
    }
}
