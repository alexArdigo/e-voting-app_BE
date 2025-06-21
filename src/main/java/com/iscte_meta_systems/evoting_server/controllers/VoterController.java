package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.services.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoterController {

    @Autowired
    private VoterService voterService;

    @GetMapping("/voters/has-voted")
    public Boolean hasAlreadyVoted(@RequestBody Long voter) {
        return voterService.hasAlreadyVoted(voter);
    }
}
