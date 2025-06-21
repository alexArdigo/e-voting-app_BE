package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Organisation;
import com.iscte_meta_systems.evoting_server.entities.Party;
import com.iscte_meta_systems.evoting_server.entities.UniParty;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;

import java.util.List;

public interface OrganisationService {

    List<Organisation> getAllOrganisations(String election, String electoralCircle);

    Organisation getOrganisationById(Long id);

    OrganisationDTO createOrganisation(OrganisationDTO organisationDTO);

    List<Party> getAllParties();

    List<UniParty> getAllUniParties();
}

