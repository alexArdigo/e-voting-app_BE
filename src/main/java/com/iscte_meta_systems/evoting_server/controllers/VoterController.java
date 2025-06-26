package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
import com.iscte_meta_systems.evoting_server.services.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoterController {

    @Autowired
    private VoterService voterService;

    @PostMapping("/voters/authenticated")
    public void saveVoterAuthenticated(@RequestBody VoterDTO voterDTO) {
        System.out.println("voterDTO.getNif() = " + voterDTO.getNif());
        System.out.println("voterDTO.getDistrict() = " + voterDTO.getDistrict());
        System.out.println("voterDTO.getMunicipality() = " + voterDTO.getMunicipality());
        System.out.println("voterDTO.getParish() = " + voterDTO.getParish());
        voterService.saveVoterAuthenticated(voterDTO);
    }

    @GetMapping("/voters/has-voted")
    public ResponseEntity<?> hasAlreadyVoted(@RequestBody String voter, Long electionId) {
        return ResponseEntity.ok().body(voterService.hasAlreadyVoted(voter, electionId));
    }
}
