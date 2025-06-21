package com.iscte_meta_systems.evoting_server.controllers;


import com.iscte_meta_systems.evoting_server.entities.Candidate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CandidateController {

    @GetMapping("/candidates")
    public List<Candidate> getCandidates(String candidateType, String electionType) {
        return candidateService.getCandidatesByType(candidateType, electionType);
    }
}
