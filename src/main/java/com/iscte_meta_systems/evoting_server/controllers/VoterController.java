package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
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

    @PostMapping("/voters/start-voting")
    public ResponseEntity<?> startVoting(
            @RequestParam("electionId") Long electionId,
            @RequestParam("voterId") Long voterId
    ) {
        return ResponseEntity.ok().body(voterService.startVoting(electionId, voterId));
    }

    @GetMapping("/voters/{id}/is-voting")
    public ResponseEntity<?> isVoting(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok().body(voterService.isVoting(id));
    }

    @PostMapping("/voters/stop-voting")
    public ResponseEntity<?> stopVoting(
            @RequestParam("electionId") Long electionId,
            @RequestParam("voterId") Long voterId
    ) {
        voterService.stopVoting(electionId, voterId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/voters/has-voted")
    public ResponseEntity<?> hasAlreadyVoted(@RequestParam("nif") String nif) {
        return ResponseEntity.ok().body(voterService.hasAlreadyVoted(nif));
    }
}
