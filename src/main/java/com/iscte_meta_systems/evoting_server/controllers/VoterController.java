package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
import com.iscte_meta_systems.evoting_server.services.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoterController {

    @Autowired
    private VoterService voterService;

    @GetMapping("/voters")
    public ResponseEntity<?> getVoter() {
        return ResponseEntity.ok().body(voterService.getLoggedVoter());
    }

    @GetMapping("/voters/has-voted")
    public ResponseEntity<?> hasAlreadyVoted(@RequestBody String hash, Long electionId) {
        return ResponseEntity.ok().body(voterService.hasAlreadyVoted(hash, electionId));
    }
}
