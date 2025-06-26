package com.iscte_meta_systems.evoting_server.services;
import com.iscte_meta_systems.evoting_server.model.PartyVoteStatsDTO;
import java.util.List;

public interface StatisticsService {

    public List<PartyVoteStatsDTO> getVotePercentagesByPartyByDistrict(Long electionId, String districtName);

    public int getTotalVotesByPartyByDistrict(String partyName, String districtName);

    public int getTotalVotesByElection(Long electionId);


}
