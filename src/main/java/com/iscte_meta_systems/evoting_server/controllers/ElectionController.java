package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Organisation;
import com.iscte_meta_systems.evoting_server.entities.Vote;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ElectionController {

    @Autowired
    private ElectionService electionService;

    @GetMapping("/elections")
    public List<ElectionDTO> getElections(
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
    public ElectionDTO createElection(@RequestBody ElectionDTO electionDTO) {
        return electionService.createElection(electionDTO);
    }

    @GetMapping("/elections/{id}/ballot")
    public List<Organisation> getBallotByElectionId(@PathVariable Long id){
        return electionService.getBallotByElectionId(id);
    }

    @PostMapping("/elections/{id}/castVote")
    public Vote castVote(@PathVariable Long id, @RequestBody Vote vote) {
        return electionService.castVote(id, vote);
    }

    @PostMapping("/elections/{id}/startElection")
    public Election startElection(@PathVariable Long id) {
        return electionService.startElection(id);
    }

    @PostMapping("/elections/{id}/endElection")
    public Election endElection(@PathVariable Long id) {
        return electionService.endElection(id);
    }

    @GetMapping("/{id}/isStarted")
    public boolean isElectionStarted(@PathVariable Long id) {
        return electionService.isStarted(id);
    }

    @GetMapping("/elections/all")
    public List<Election> getAllElections() {
        return electionService.getAllElections();
    }

    @GetMapping("/election/active")
    public List<Election> getActiveElections() {
        return electionService.getActiveElections();
    }

}
