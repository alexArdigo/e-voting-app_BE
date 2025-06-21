package com.iscte_meta_systems.evoting_server.controllers;
import com.iscte_meta_systems.evoting_server.entities.Organisation;
import com.iscte_meta_systems.evoting_server.entities.Party;
import com.iscte_meta_systems.evoting_server.entities.UniParty;
import com.iscte_meta_systems.evoting_server.services.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrganisationController {

    @Autowired
    private OrganisationService organisationService;

    @GetMapping("/organisations")
    public List<Organisation> getOrganisation(
        @RequestParam(required = false) String election,
        @RequestParam(required = false) String electorateCircle
    ) {
        return organisationService.getAllOrganisations(election, electorateCircle);
    }

    @GetMapping("/parties")
    public List<Party> getAllParties() {
        return organisationService.getAllParties();
    }

    @GetMapping("/uniparties")
    public List<UniParty> getAllUniParties() {
        return organisationService.getAllUniParties();
    }

    @GetMapping("/organisations/{id}")
    public Organisation getOrganisationByIdById(@PathVariable Long id) {
        return organisationService.getOrganisationById(id);
    }

    @PostMapping("/organisations")
    public Organisation createOrganisation(@RequestBody Organisation organisation) {
        return organisationService.createOrganisation(organisation);
    }


}
