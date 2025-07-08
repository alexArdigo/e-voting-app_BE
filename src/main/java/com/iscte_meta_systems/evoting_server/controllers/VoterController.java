package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.services.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VoterController {

    @Autowired
    private VoterService voterService;

    @GetMapping("/loggedVoter")
    public ResponseEntity<?> getLoggedVoter() {
        return ResponseEntity.ok().body(voterService.getLoggedVoter());
    }

    @PostMapping("/voters/has-voted")
        public ResponseEntity<?> hasAlreadyVoted(@RequestParam("nif") String nif) {
        return ResponseEntity.ok().body(voterService.hasAlreadyVoted(nif));
    }
}
