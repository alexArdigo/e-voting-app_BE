package com.iscte_meta_systems.evoting_server.controllers;


import com.iscte_meta_systems.evoting_server.entities.Candidate;
import com.iscte_meta_systems.evoting_server.services.CandidateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CandidateController {

    CandidateService candidateService;


    @GetMapping("/candidates")
    public List<Candidate> getCandidates(
            @RequestParam (required = false) String candidateType,
            @RequestParam (required = false) String electionType
    ) {
        return candidateService.getCandidatesByType(candidateType, electionType);
    }

    @GetMapping("/candidates/{id}")
    public Candidate getCandidateById(@PathVariable long id) {
        return candidateService.getCandidatesById(id);
    }
    @PostMapping("/candidates")
    public Candidate addCandidate(@RequestBody Candidate candidate) {
        return candidateService.addCandidate(candidate);
    }

}
