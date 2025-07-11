package com.iscte_meta_systems.evoting_server.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.Voter;
import com.iscte_meta_systems.evoting_server.services.OAuthService;
import com.iscte_meta_systems.evoting_server.services.UserService;
import com.iscte_meta_systems.evoting_server.services.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {


    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private VoterService voterService;

    @Autowired
    private UserService userService;


    @GetMapping("/login") // /oauth/login - this will redirect to the Chave Móvel Digital login page
    public ResponseEntity<?> login() {
        return ResponseEntity.ok().body(oAuthService.authUrl());
    }

    @PostMapping("/auth-with-token")
    public ResponseEntity<?> authWithToken(@RequestParam String token
    ) {
        oAuthService.authWithToken(token);
        Voter voter = voterService.getLoggedVoter();
        return ResponseEntity.ok().body(voter);
    }

    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestBody JsonNode payload) {
        return ResponseEntity.ok().body(oAuthService.callback(payload));
    }

    @GetMapping("/logged")
    public ResponseEntity<?> getLogged() {
        User user = userService.getLoggedUser();
        Voter voter = voterService.getLoggedVoter();

        if (user != null) {
            return ResponseEntity.ok().body(user);
        }
        if (voter != null) {
            return ResponseEntity.ok().body(voter);
        }
        return ResponseEntity.status(204).body("No user or voter is logged in.");

    }
}
