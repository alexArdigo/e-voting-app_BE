package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.model.ElectionResultDTO;
import com.iscte_meta_systems.evoting_server.model.LegislativeResultDTO;
import com.iscte_meta_systems.evoting_server.services.ResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ResultsController {

    @Autowired
    private ResultsService resultsService;

    @GetMapping("/elections/{id}/results/presidential")
    public ElectionResultDTO getPresidentialResults(@PathVariable Long id) {
        return resultsService.getPresidentialResults(id);
    }

    @GetMapping("/electoralcircles/{id}/results")
    public LegislativeResultDTO getLegislativeResults(@PathVariable Long id) {
        return resultsService.getLegislativeResults(id);
    }

    @GetMapping("/Elections/{id}/results/legislative")
    public List<LegislativeResultDTO> getAllLegislativeResults(@PathVariable Long id) {
        return resultsService.getAllLegislativeResults(id);
    }
}
