package com.iscte_meta_systems.evoting_server.services;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface OAuthService {
    Map<String, String> authUrl();

    String callback(JsonNode payload);

    void getAccessToken(String token);

    void addVoter(JsonNode userInfo);
}
