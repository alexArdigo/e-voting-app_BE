package com.iscte_meta_systems.evoting_server.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.iscte_meta_systems.evoting_server.services.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {


    @Autowired
    private OAuthService oAuthService;


    @GetMapping("/login") // /oauth/login - this will redirect to the Chave Móvel Digital login page
    public ResponseEntity<?> login() {
        return ResponseEntity.ok().body(oAuthService.authUrl());
    }

    @PostMapping("/auth-with-token")
    public ResponseEntity<?> authWithToken(
            @RequestParam String token,
            @RequestParam Long id
    ) {
        return ResponseEntity.ok().body(oAuthService.authWithToken(token, id));
    }

    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestBody JsonNode payload) {
        return ResponseEntity.ok().body(oAuthService.callback(payload));
    }
}
