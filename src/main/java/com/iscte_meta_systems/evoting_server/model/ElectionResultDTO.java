package com.iscte_meta_systems.evoting_server.model;

import java.util.List;

public class ElectionResultDTO {


    private String electionName;
    private String electionType;
    private int totalVotes;
    private List<OrganisationResultDTO> results;

    public ElectionResultDTO() {}

    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public String getElectionType() {
        return electionType;
    }

    public void setElectionType(String electionType) {
        this.electionType = electionType;
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