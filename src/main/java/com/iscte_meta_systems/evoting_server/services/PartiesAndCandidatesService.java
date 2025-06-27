package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.ElectoralCircle;

public interface PartiesAndCandidatesService {
    void populatePartiesAndCandidatesFromJSON(ElectoralCircle electoralCircle);
}