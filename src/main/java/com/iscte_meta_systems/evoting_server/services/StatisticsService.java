package com.iscte_meta_systems.evoting_server.services;
import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Party;
import com.iscte_meta_systems.evoting_server.model.PartyVoteStatsDTO;

import java.util.List;

import com.iscte_meta_systems.evoting_server.model.DistrictStatisticsDTO;

public interface StatisticsService {

    DistrictStatisticsDTO getDistrictStatistics(String districtName);

    public List<PartyVoteStatsDTO> getVotePercentagesByPartyByDistrict(Long electionId, String districtName);

    public int getTotalVotesByElection(Long electionId);

    public int getVotesByPartyByElectoralCircle(String partyName, Long electoralCircleId);

    public int getVotesByPartyByDistrict(String partyName, String districtName, int year);

    public int getGlobalVotesByPartyByYearOfElection(String partyName, int year);

    public List<Party> getParties();

}
