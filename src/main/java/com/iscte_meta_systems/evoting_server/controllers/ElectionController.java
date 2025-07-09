package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.model.LegislativeDTO;
import com.iscte_meta_systems.evoting_server.model.VoteRequestModel;
import com.iscte_meta_systems.evoting_server.repositories.ElectoralCircleRepository;
import com.iscte_meta_systems.evoting_server.services.ElectionService;
import com.iscte_meta_systems.evoting_server.services.PartiesAndCandidatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ElectionController {

    @Autowired
    private ElectionService electionService;

    @Autowired
    private ElectoralCircleRepository electoralCircleRepository;

    @Autowired
    private PartiesAndCandidatesService partiesAndCandidatesService;

    @GetMapping("/elections")
    public List<ElectionDTO> getPresidentialOrElectoralCircle(
            @RequestParam(required = false) String electionType,
            @RequestParam(required = false) Integer electionYear,
            @RequestParam(required = false) Boolean isActive
    ) {
        return electionService.getPresidentialOrElectoralCircle(electionType, electionYear, isActive);
    }

    @GetMapping("/elections/legislative")
    public List<Legislative> getLegislativeElections(
            @RequestParam(required = false) Integer electionYear,
            @RequestParam(required = false) Boolean isActive
    ) {
        return electionService.getLegislativeElections(electionYear, isActive);
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
    public List<Organisation> getBallotByElectionId(@PathVariable Long id) {
        return electionService.getBallotByElectionId(id);
    }

    @PostMapping("/elections/{id}/castVote")
    public Vote castVote(@PathVariable Long id, @RequestBody VoteRequestModel vote) {
        return electionService.castVote(id, vote);
    }

    @GetMapping("/elections/{id}/isStarted")
    public boolean isElectionStarted(@PathVariable Long id) {
        return electionService.isStarted(id);
    }

    @PostMapping("/elections/{id}/populate-parties")
    public String populatePartiesAndCandidates(@PathVariable Long id) {
        try {
            ElectoralCircle electoralCircle = electoralCircleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Electoral circle not found"));

            partiesAndCandidatesService.populatePartiesAndCandidatesFromJSON(electoralCircle);

            return "Parties and candidates populated successfully";
        } catch (Exception e) {
            return "Error populating parties and candidates: " + e.getMessage();
        }
    }

    @PostMapping("/elections/testVotes/{numberOfVotes}/{electionId}")
    public List<Vote> generateTestVotes(@PathVariable int numberOfVotes, @PathVariable Long electionId) {
        return electionService.generateTestVotes(numberOfVotes, electionId);
    }

    @GetMapping("/legislatives/{id}")
    public Legislative getLegislativeById(@PathVariable Long id) {
        return electionService.getLegislativeById(id);
    }

    @PutMapping("/elections/presidential/{id}")
    public ElectionDTO updatePresidentialElection(@PathVariable Long id, @RequestBody ElectionDTO electionDTO) {
        return electionService.updatePresidentialElection(id, electionDTO);
    }

    @DeleteMapping("/elections/presidential/{id}")
    public ResponseEntity<String> deletePresidentialElection(@PathVariable Long id) {
        try {
            electionService.deletePresidentialElection(id);
            return ResponseEntity.ok("Presidential election deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting presidential election: " + e.getMessage());
        }
    }

    @PutMapping("/elections/legislative/{id}")
    public Legislative updateLegislativeElection(@PathVariable Long id, @RequestBody LegislativeDTO legislativeDTO) {
        return electionService.updateLegislativeElection(id, legislativeDTO);
    }

    @DeleteMapping("/elections/legislative/{id}")
    public ResponseEntity<String> deleteLegislativeElection(@PathVariable Long id) {
        try {
            electionService.deleteLegislativeElection(id);
            return ResponseEntity.ok("Legislative election deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting legislative election: " + e.getMessage());
        }
    }

    @PutMapping("/elections/electoral-circle/{id}")
    public ElectoralCircle updateElectoralCircle(@PathVariable Long id, @RequestBody ElectoralCircleDTO electoralCircleDTO) {
        return electionService.updateElectoralCircle(id, electoralCircleDTO);
    }

    @DeleteMapping("/elections/electoral-circle/{id}")
    public ResponseEntity<String> deleteElectoralCircle(@PathVariable Long id) {
        try {
            electionService.deleteElectoralCircle(id);
            return ResponseEntity.ok("Electoral circle deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting electoral circle: " + e.getMessage());
        }
    }

    @Deprecated
    @PutMapping("/elections/{id}")
    public ElectionDTO updateElection(@PathVariable Long id, @RequestBody ElectionDTO electionDTO) {
        return electionService.updateElection(id, electionDTO);
    }

    @Deprecated
    @DeleteMapping("/elections/{id}")
    public ResponseEntity<String> deleteElection(@PathVariable Long id) {
        try {
            electionService.deleteElection(id);
            return ResponseEntity.ok("Election deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting election: " + e.getMessage());
        }
    }
}