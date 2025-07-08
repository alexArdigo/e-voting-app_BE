package com.iscte_meta_systems.evoting_server.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.iscte_meta_systems.evoting_server.entities.Voter;

import java.util.Map;

public interface OAuthService {
    Map<String, String> authUrl();

    Long addVoter(JsonNode userInfo, String token);

    Voter authWithToken(String token, Long voterId);

    Long callback(JsonNode payload);
}
