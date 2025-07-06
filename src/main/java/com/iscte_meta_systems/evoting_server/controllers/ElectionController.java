package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.ElectoralCircle;
import com.iscte_meta_systems.evoting_server.entities.Organisation;
import com.iscte_meta_systems.evoting_server.entities.Vote;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.repositories.ElectoralCircleRepository;
import com.iscte_meta_systems.evoting_server.services.ElectionService;
import com.iscte_meta_systems.evoting_server.services.PartiesAndCandidatesService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{id}/isStarted")
    public boolean isElectionStarted(@PathVariable Long id) {
        return electionService.isStarted(id);
    }

    @GetMapping("/elections/all")
    public List<Election> getAllElections() {
        return electionService.getAllElections();
    }

    @GetMapping("/election/active")
    public List<ElectionDTO> getActiveElections() {
        return electionService.getActiveElections();
    }

    @GetMapping("/election/notactive")
    public List<Election> getNotActiveElections() {
        return electionService.getNotActiveElections();
    }

    @PostMapping ("/election/testVotes/{numberOfVotes}/{electionId}")
    public List<Vote> generateTestVotes(@PathVariable int numberOfVotes, @PathVariable Long electionId) {
        return electionService.generateTestVotes(numberOfVotes, electionId);
    }

}
