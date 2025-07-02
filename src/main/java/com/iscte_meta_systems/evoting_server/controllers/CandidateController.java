package com.iscte_meta_systems.evoting_server.controllers;


import com.iscte_meta_systems.evoting_server.entities.Candidate;
import com.iscte_meta_systems.evoting_server.model.CandidateDTO;
import com.iscte_meta_systems.evoting_server.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CandidateController {

    @Autowired
    CandidateService candidateService;


    @GetMapping("/elections/{id}/candidates")
    public List<CandidateDTO> getCandidates(@PathVariable Long id) {
        return candidateService.getCandidatesByElection(id);
    }

    @GetMapping("/candidate/{id}")
    public CandidateDTO getCandidateById(@PathVariable Long id) {
        return candidateService.getCandidatesById(id);
    }

    @PostMapping("/candidates")
    public Candidate addCandidate(@RequestBody CandidateDTO candidate) {
        return candidateService.addCandidate(candidate);
    }

}
