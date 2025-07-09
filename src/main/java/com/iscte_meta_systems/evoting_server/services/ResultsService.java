package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.model.ElectionResultDTO;
import com.iscte_meta_systems.evoting_server.model.LegislativeResultDTO;

import java.util.List;
import java.util.Map;

public interface ResultsService {

    ElectionResultDTO getPresidentialResults(Long electionId);

    LegislativeResultDTO getLegislativeResults(Long electionId);

    List<LegislativeResultDTO> getAllLegislativeResults(Long electionId);

    public List<LegislativeResultDTO> getAllLegislativeResultsByYear(int year);

    public Map<String, Integer> getLegislativeSeatsByPartyForYear(int year);
}