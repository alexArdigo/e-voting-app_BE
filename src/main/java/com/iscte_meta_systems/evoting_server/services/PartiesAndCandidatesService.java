package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.ElectoralCircle;
import com.iscte_meta_systems.evoting_server.entities.Party;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;

import java.util.List;

public interface PartiesAndCandidatesService {
    void populatePartiesAndCandidatesFromJSON(ElectoralCircle electoralCircle);

    void populatePartiesAndCandidatesFromJSONWithDTO(ElectionDTO electionDTO);
}