package com.iscte_meta_systems.evoting_server.services;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface OAuthService {
    String authUrl();

    Map<String, String> callback(String code, HttpSession session);

    String getAccessToken(String token);

    void addVoter(JsonNode userInfo);
}
