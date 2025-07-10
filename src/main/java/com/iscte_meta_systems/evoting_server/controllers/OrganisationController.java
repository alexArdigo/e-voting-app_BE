package com.iscte_meta_systems.evoting_server.controllers;
import com.iscte_meta_systems.evoting_server.entities.Organisation;
import com.iscte_meta_systems.evoting_server.entities.Party;
import com.iscte_meta_systems.evoting_server.entities.UniParty;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;
import com.iscte_meta_systems.evoting_server.services.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrganisationController {

    @Autowired
    private OrganisationService organisationService;

    @GetMapping("/organisations")
    public List<Organisation> getOrganisation(
        @RequestParam(required = false) String election,
        @RequestParam(required = false) String electoralCircle
    ) {
        return organisationService.getAllOrganisations(election, electoralCircle);
    }

    @GetMapping("/parties")
    public List<Party> getAllParties() {
        return organisationService.getAllParties();
    }

    @GetMapping("/parties/{id}")
    public ResponseEntity<?> getPartyById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(organisationService.getPartyById(id));
    }

    @PostMapping("/parties")
    public ResponseEntity<?> addParty(
            @RequestParam() String name,
            @RequestParam() String color,
            @RequestParam() String imageURL,
            @RequestParam() String description
    ) {
        return ResponseEntity.ok().body(organisationService.addParty(
                name,
                color,
                imageURL,
                description
        ));
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
    public OrganisationDTO createOrganisation(@RequestBody OrganisationDTO organisationDTO) {
        return organisationService.createOrganisation(organisationDTO);
    }

    @PutMapping("/organisations/{id}")
    public OrganisationDTO updateOrganisation(@PathVariable Long id, @RequestBody OrganisationDTO organisationDTO) {
        return organisationService.updateOrganisation(id, organisationDTO);
    }
}
