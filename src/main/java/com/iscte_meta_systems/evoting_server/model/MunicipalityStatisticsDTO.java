package com.iscte_meta_systems.evoting_server.model;

import java.util.List;

public class MunicipalityStatisticsDTO {
    private String municipalityName;
    private int totalVotes;
    private List<PartyVoteStatsDTO> partyResults;

    public MunicipalityStatisticsDTO() {}

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public List<PartyVoteStatsDTO> getPartyResults() {
        return partyResults;
    }

    public void setPartyResults(List<PartyVoteStatsDTO> partyResults) {
        this.partyResults = partyResults;
    }
}