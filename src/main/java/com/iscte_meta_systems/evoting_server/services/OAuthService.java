package com.iscte_meta_systems.evoting_server.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.iscte_meta_systems.evoting_server.entities.OAuthToken;

import java.util.Map;

public interface OAuthService {
    Map<String, String> authUrl();

    Long addVoter(JsonNode userInfo, OAuthToken token);

    void authWithToken(String token);

    Long callback(JsonNode payload);
}
