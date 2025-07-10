package com.iscte_meta_systems.evoting_server.services;
import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Organisation;
import com.iscte_meta_systems.evoting_server.entities.Party;
import com.iscte_meta_systems.evoting_server.entities.UniParty;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;
import com.iscte_meta_systems.evoting_server.repositories.ElectionRepository;
import com.iscte_meta_systems.evoting_server.repositories.OrganisationRepository;
import com.iscte_meta_systems.evoting_server.repositories.PartyRepository;
import com.iscte_meta_systems.evoting_server.repositories.UniPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.iscte_meta_systems.evoting_server.enums.OrganisationType.PARTY;
import static com.iscte_meta_systems.evoting_server.enums.OrganisationType.UNIPARTY;

@Service
public class OrganisationServiceImpl implements OrganisationService {

    @Autowired
    OrganisationRepository organisationRepository;

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    UniPartyRepository uniPartyRepository;

    @Autowired
    ElectionService electionService;

    @Autowired
    ElectionRepository electionRepository;

    @Override
    public List<Organisation> getAllOrganisations(String election, String electoralCircle) {

        return organisationRepository.findAll().stream()
                .filter(org -> election == null ||
                        (org.getElection() != null && org.getElection().getName().equalsIgnoreCase(election)))
                .collect(Collectors.toList());
    }

    @Override
    public Organisation getOrganisationById(Long id) {
        return organisationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Organisation not found with ID " + id));
    }

    @Override
    public OrganisationDTO createOrganisation(OrganisationDTO organisationDTO) {
        if (organisationDTO.getName() == null || organisationDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Organisation name is required.");
        }
        Organisation organisation;
        switch (organisationDTO.getOrganisationType()) {
            case PARTY -> organisation = new Party();
            case UNIPARTY -> organisation = new UniParty();
            default -> throw new IllegalArgumentException("Unknown organisation type: " + organisationDTO.getOrganisationType());
        }

        Election election = electionService.getElectionById(organisationDTO.getElectionId());

        organisation.setOrganisationName(organisationDTO.getName());
        election.addOrganisation(organisation);

        electionRepository.save(election);

        return organisationDTO;
    }


    @Override
    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }

    @Override
    public Party getPartyById(Long id) {
        return partyRepository.findPartyById(id);
    }

    @Override
    public Party addParty(String name, String color, String imageURL, String description) {
        return partyRepository.save(new Party(name, color, imageURL, description));
    }

    @Override
    public List<UniParty> getAllUniParties() {
        return uniPartyRepository.findAll();
    }

    @Override
    public OrganisationDTO updateOrganisation(Long id, OrganisationDTO organisationDTO) {
        return null;
    }

}
