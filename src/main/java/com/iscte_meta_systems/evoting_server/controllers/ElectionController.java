package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ElectionController {

    @Autowired
    private ElectionService electionService;

    @GetMapping("/elections")
    public List<Election> getElections(
            @RequestParam(required = false) String electionType,
            @RequestParam(required = false) Integer electionYear
    ) {
        return electionService.getElections(electionType, electionYear);
    }

    @GetMapping("/elections/{id}")
    public Election getElectionById(@PathVariable Long id) {
        return electionService.getElectionById(id);
    }

    @PostMapping("/elections")
    public Election createElection(@RequestBody Election election) {
        return electionService.createElection(election);
    }

//    @GetMapping("/elections/{id}/ballot")
//    public List<Candidate> getBallotByElectionId(@PathVariable Long id){
//        return ElectionService.getBallotByElectionId(id);
//    }
//
//    @PostMapping("/elections/{id}/castVote")
//    public Vote castVote(@PathVariable Long id, @RequestBody Vote vote) {
//        return electionService.castVote(id, vote);
//    }

    @PostMapping("/elections/{id}/startElection")
    public Election startElection(@PathVariable Long id) {
        return electionService.startElection(id);
    }
}
