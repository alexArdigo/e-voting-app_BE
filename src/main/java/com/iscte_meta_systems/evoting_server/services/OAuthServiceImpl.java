package com.iscte_meta_systems.evoting_server.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscte_meta_systems.evoting_server.controllers.VoterController;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class OAuthServiceImpl implements OAuthService {

    WebClient webClient;

    public OAuthServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

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


    @Override
    public String authUrl() {
        return chavemovelUrl + "/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri;
    }

    @Override
    public Map<String, String> callback(String code, HttpSession session) {
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
            throw new RuntimeException("Failed to retrieve access token");
        }

        String accessToken = getAccessToken(token);

        session.setAttribute("access_token", accessToken);

        String redirectUrl = "http://localhost:5173/voter-data";

        return Map.of("redirect", redirectUrl);
    }

    @Override
    public String getAccessToken(String token) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode tokenJson = objectMapper.readTree(token);

            // Extract user info
            JsonNode voterInfo = tokenJson.get("user");

            addVoter(voterInfo);

            // Extract access token
            return tokenJson.get("access_token").get("token").asText();

        } catch (Exception e) {
            System.err.println("Error parsing token response: " + e.getMessage());
            throw new RuntimeException("Failed to parse token response", e);
        }
    }


    @Override
    public void addVoter(JsonNode userInfo) {
        VoterDTO voterDTO = new VoterDTO(userInfo);
        voterController.saveVoterAuthenticated(voterDTO);
    }
}
