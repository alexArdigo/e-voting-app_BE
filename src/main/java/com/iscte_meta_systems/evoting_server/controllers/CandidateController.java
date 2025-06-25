package com.iscte_meta_systems.evoting_server.controllers;


import com.iscte_meta_systems.evoting_server.entities.Candidate;
import com.iscte_meta_systems.evoting_server.model.CandidateDTO;
import com.iscte_meta_systems.evoting_server.services.CandidateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CandidateController {

    CandidateService candidateService;


    @GetMapping("/elections/{id}/candidates")
    public List<CandidateDTO> getCandidates(@PathVariable Long id) {
        return candidateService.getCandidatesByElection(id);
    }

    @GetMapping("/candidate/{id}")
    public Candidate getCandidateById(@PathVariable long id) {
        return candidateService.getCandidatesById(id);
    }
    @PostMapping("/candidates")
    public Candidate addCandidate(@RequestBody Candidate candidate) {
        return candidateService.addCandidate(candidate);
    }

}
