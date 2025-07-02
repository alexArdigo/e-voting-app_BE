package com.iscte_meta_systems.evoting_server.model;

public class VoteRequestModel {
    private Long voterNif;
    private Long electionId;
    private Long organisationId;
    private String municipalityName;

    public VoteRequestModel(Long voterNif, Long electionId, Long organisationId) {
        this.voterNif = voterNif;
        this.electionId = electionId;
        this.organisationId = organisationId;
    }

    public Long getVoterNif() {
        return voterNif;
    }

    public void setVoterNif(Long voterNif) {
        this.voterNif = voterNif;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }
}
