package com.iscte_meta_systems.evoting_server.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
import com.iscte_meta_systems.evoting_server.services.OAuthService;
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


    @Autowired
    private OAuthService oAuthService;


    @GetMapping("/login") // /oauth/login - this will redirect to the Chave Móvel Digital login page
    public RedirectView login() {
        return new RedirectView(oAuthService.authUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code, HttpSession session) {
        return ResponseEntity.ok().body(oAuthService.callback(code, session));
    }

}
