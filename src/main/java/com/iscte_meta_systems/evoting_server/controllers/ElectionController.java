package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
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
    public List<ElectionDTO> getElections(
            @RequestParam(required = false) String electionType,
            @RequestParam(required = false) Integer electionYear,
            @RequestParam(required = false) Boolean isActive
    ) {
        return electionService.getElections(electionType, electionYear, isActive);
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

    @PostMapping("/elections/{id}/startElection")
    public Election startElection(@PathVariable Long id) {
        return electionService.startElection(id);
    }

    @PostMapping("/elections/{id}/endElection")
    public Election endElection(@PathVariable Long id) {
        return electionService.endElection(id);
    }

    @GetMapping("/elections/{id}/isStarted")
    public boolean isElectionStarted(@PathVariable Long id) {
        return electionService.isStarted(id);
    }

    @PostMapping("/elections/{id}/populate-parties")
    public String populatePartiesAndCandidates(@PathVariable Long id) {
        try {
            ElectoralCircle electoralCircle = electoralCircleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Electoral Circle not found"));

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

    @GetMapping("/legislatives")
    public List<Legislative> getLegislatives() {
        return electionService.getLegislatives();
    }

    @GetMapping("/legislatives/{id}")
    public Legislative getLegislativeById(@PathVariable Long id) {
        return electionService.getLegislativeById(id);
    }

    @GetMapping("/elections/active")
    public List<ElectionDTO> getActiveElections() {
        return electionService.getElections(null, null, true);
    }

    @GetMapping("/elections/inactive")
    public List<ElectionDTO> getInactiveElections() {
        return electionService.getElections(null, null, false);
    }

    @PutMapping("/elections/{id}")
    public ElectionDTO updateElection(@PathVariable Long id, @RequestBody ElectionDTO electionDTO) {
        return electionService.updateElection(id, electionDTO);
    }

    @DeleteMapping("/elections/{id}")
    public ResponseEntity<String> deleteElection(@PathVariable Long id) {
        try {
            electionService.deleteElection(id);
            return ResponseEntity.ok("Eleição apagada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao apagar eleição: " + e.getMessage());
        }
    }
}