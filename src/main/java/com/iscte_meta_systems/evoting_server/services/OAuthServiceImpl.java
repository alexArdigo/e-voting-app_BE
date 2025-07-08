package com.iscte_meta_systems.evoting_server.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.iscte_meta_systems.evoting_server.controllers.VoterController;
import com.iscte_meta_systems.evoting_server.entities.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.UUID;

@Service
public class OAuthServiceImpl implements OAuthService {

    WebClient webClient;

    public OAuthServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    @Autowired
    private VoterRepository voterRepository;

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


        String requestUrl = chavemovelUrl + "/oauth/token";
        restTemplate.postForObject(requestUrl, new HttpEntity<>(tokenRequest.toString(), headers), String.class);
    }

    @Override
    @Transactional
    public void authWithToken(String token, Long voterId) {
        OAuthToken existingOAuthToken = oAuthTokenRepository.findOAuthTokenByToken(token);

        if (existingOAuthToken == null) {
            throw new IllegalArgumentException("Invalid OAuth token");
        }

        authenticationToken(voterId.toString(), existingOAuthToken.getToken());

        oAuthTokenRepository.deleteByToken(existingOAuthToken.getToken());

    }

    private UsernamePasswordAuthenticationToken authenticationToken(String voterId, String token) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(voterId, token);
        Authentication authentication = voterAuthenticationProvider.authenticate(authToken);

        if (authentication != null && authentication.isAuthenticated()) {
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return authToken;
    }

    @Override
    public Long callback(JsonNode payload) {
        if (payload == null || !payload.has("token") || !payload.has("user")) {
            throw new IllegalArgumentException("Invalid payload");
        }

        String token = payload.get("token").asText();

        OAuthToken oAuthToken = oAuthTokenRepository.findOAuthTokenByToken(token);
        if (oAuthToken == null) {
            throw new IllegalArgumentException("Invalid OAuth token");
        }

        JsonNode user = payload.get("user");

        return addVoter(user, token);
    }

    @Override
    public Long addVoter(JsonNode user, String token) {
        Voter existingVoter = voterRepository.findVoterByNif(user.path("nif").asLong());

        if (existingVoter != null) {
            return existingVoter.getId();
        }

        District district = voterService.getDistrict(user.path("district").asText());
        Municipality municipality = voterService.getMunicipality(user.path("municipality").asText(), district);
        Parish parish = voterService.getParish(user.path("parish").asText(), municipality);

        Voter voter = voterRepository.save(new Voter(user, district, municipality, parish));
        voterService.saveVoterHash(voter);

        return voter.getId();
    }
}
