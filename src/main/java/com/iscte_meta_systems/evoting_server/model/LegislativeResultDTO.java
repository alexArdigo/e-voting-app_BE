package com.iscte_meta_systems.evoting_server.model;

import java.util.List;

public class LegislativeResultDTO {

    private String electionName;
    private String districtName;
    private int totalSeats;
    private int totalVotes;
    private int blankVotes;
    private List<OrganisationResultDTO> results;

    public LegislativeResultDTO() {}

    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public int getBlankVotes() {
        return blankVotes;
    }

    public void setBlankVotes(int blankVotes) {
        this.blankVotes = blankVotes;
    }

    public List<OrganisationResultDTO> getResults() {
        return results;
    }

    public void setResults(List<OrganisationResultDTO> results) {
        this.results = results;
    }
}