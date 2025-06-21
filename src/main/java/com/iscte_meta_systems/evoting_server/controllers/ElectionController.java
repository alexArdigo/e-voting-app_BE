package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Election getElectionById(Long id) {
        return electionService.getElectionById(id);
    }
}
