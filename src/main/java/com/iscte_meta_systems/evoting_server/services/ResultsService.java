package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.model.ElectionResultDTO;
import com.iscte_meta_systems.evoting_server.model.LegislativeResultDTO;

import java.util.List;

public interface ResultsService {

    ElectionResultDTO getPresidentialResults(Long electionId);

    LegislativeResultDTO getLegislativeResults(Long electionId);

    List<LegislativeResultDTO> getAllLegislativeResults();
}