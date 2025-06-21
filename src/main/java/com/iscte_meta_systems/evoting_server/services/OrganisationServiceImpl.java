package com.iscte_meta_systems.evoting_server.services;
import com.iscte_meta_systems.evoting_server.entities.Organisation;
import com.iscte_meta_systems.evoting_server.entities.Party;
import com.iscte_meta_systems.evoting_server.entities.UniParty;
import com.iscte_meta_systems.evoting_server.enums.VotingArea;
import com.iscte_meta_systems.evoting_server.repositories.OrganisationRepository;
import com.iscte_meta_systems.evoting_server.repositories.PartyRepository;
import com.iscte_meta_systems.evoting_server.repositories.UniPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganisationServiceImpl implements OrganisationService {

    @Autowired
    OrganisationRepository organisationRepository;

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    UniPartyRepository uniPartyRepository;

    @Override
    public List<Organisation> getAllOrganisations(String election, String electoralCircle) {
        VotingArea area = VotingArea.valueOf(electoralCircle.toUpperCase());

        return organisationRepository.findAll().stream()
                .filter(org -> election == null ||
                        (org.getElection() != null && org.getElection().getName().equalsIgnoreCase(election)))
                .filter(org -> org.getElectoralCircle() != null &&
                        org.getElectoralCircle().getVotingArea() == area)
                .collect(Collectors.toList());
    }

    @Override
    public Organisation getOrganisationById(Long id) {
        return organisationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Organisation not found with ID " + id));
    }

    @Override
    public Organisation createOrganisation(Organisation organisation) {
//        if (party.getName() == null || organisation.getName().isEmpty()) {
//            throw new IllegalArgumentException("Organisation name is required.");
//        }
//        if (organisation.getDescription() == null || organisation.getDescription().isEmpty()) {
//            throw new IllegalArgumentException("Organisation description is required.");
//        }
        return organisationRepository.save(organisation);
    }

    @Override
    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }

    @Override
    public List<UniParty> getAllUniParties() {
        return uniPartyRepository.findAll();
    }

}
