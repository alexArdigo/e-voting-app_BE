package com.iscte_meta_systems.evoting_server.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscte_meta_systems.evoting_server.controllers.VoterController;
import com.iscte_meta_systems.evoting_server.entities.OAuthToken;
import com.iscte_meta_systems.evoting_server.entities.Voter;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
import com.iscte_meta_systems.evoting_server.repositories.OAuthTokenRepository;
import com.iscte_meta_systems.evoting_server.repositories.VoterRepository;
import com.iscte_meta_systems.evoting_server.security.VoterAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.UUID;

@Service
public class OAuthServiceImpl implements OAuthService {

    WebClient webClient;
    @Autowired
    private VoterRepository voterRepository;

    public OAuthServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    @Autowired
    private VoterAuthenticationProvider voterAuthenticationProvider;

    @Autowired
    private VoterService voterService;

    @Autowired
    private VoterController voterController;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;


    @Value("${chave.movel.base-url}")
    private String chavemovelUrl;

    @Value("${oauth.client.id}")
    private String clientId;

    @Value("${oauth.client.secret}")
    private String clientSecret;

    @Value("${oauth.redirect.uri}")
    private String redirectUri;


    @Override
    public Map<String, String> authUrl() {
        String token = UUID.randomUUID().toString();

        OAuthToken savedToken = oAuthTokenRepository.save(new OAuthToken(token));

        sendTokenToCMD(savedToken.getToken(), chavemovelUrl, clientId, redirectUri);

        return Map.of(
                "token", savedToken.getToken(),
                "clientId", clientId,
                "redirectUri", redirectUri
        );
    }

    private static void sendTokenToCMD(String token, String chavemovelUrl, String clientId, String redirectUri) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject tokenRequest = new JSONObject();
        tokenRequest.put("token", token);
        tokenRequest.put("clientId", clientId);
        tokenRequest.put("redirectUri", redirectUri);


        String requestUrl = chavemovelUrl + "/oauth/client-token";
        restTemplate.postForObject(requestUrl, new HttpEntity<>(tokenRequest.toString(), headers), String.class);
    }

    @Override
    public String callback(JsonNode payload) {
        String clientToken = payload.get("clientToken").asText();
        Long userId = payload.get("userId").asLong();

        boolean tokenExists = oAuthTokenRepository.existsOAuthTokenByToken(clientToken);

        if (!tokenExists)
            throw new IllegalArgumentException("Invalid or expired client token");

        String token = webClient.post()
                .uri(chavemovelUrl + "/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("user_id", userId.toString()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Failed to retrieve access token");
        }

        System.out.println("token @callback = " + token);

        getAccessToken(token);

        return "http://localhost:5173/voter-data";
    }

    @Override
    public void getAccessToken(String token) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode tokenJson = objectMapper.readTree(token);

            // Extract user info
            JsonNode userInfo = tokenJson.get("user");

            addVoter(userInfo);

            System.out.println("tokenJson = " + tokenJson);
            // Extract access token
            Long nif = tokenJson.get("PROVIDER_TOKEN").get("principal").asLong();
            String pin = tokenJson.get("PROVIDER_TOKEN").get("credentials").asText();

            authenticationToken(nif, pin);

        } catch (Exception e) {
            System.err.println("Error parsing token response: " + e.getMessage());
            throw new RuntimeException("Failed to parse token response", e);
        }
    }

    private UsernamePasswordAuthenticationToken authenticationToken(Long nif, String pin) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(nif, pin);
        Authentication authentication = voterAuthenticationProvider.authenticate(authToken);

        if (authentication != null && authentication.isAuthenticated()) {
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return authToken;
    }

    @Override
    public void addVoter(JsonNode userInfo) {
        voterRepository.save(new Voter(userInfo));
        VoterDTO voterDTO = new VoterDTO(userInfo);
        voterService.saveVoterHash(voterDTO);
    }
}
