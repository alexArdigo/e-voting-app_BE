package com.iscte_meta_systems.evoting_server.model;

import java.util.List;

public class LegislativeResultDTO {
    private Long electionId;
    private String electionName;
    private String districtName;
    private int totalSeats;
    private int totalVotes;
    private List<OrganisationResultDTO> results;

    public LegislativeResultDTO() {}

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

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

    public List<OrganisationResultDTO> getResults() {
        return results;
    }

    public void setResults(List<OrganisationResultDTO> results) {
        this.results = results;
    }
}