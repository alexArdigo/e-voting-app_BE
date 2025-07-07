package com.iscte_meta_systems.evoting_server.model;

public class VoteRequestModel {
    private Long organisationId;
    private String voterNif;
    private String municipalityName;


    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public String getVoterNif() {
        return voterNif;
    }

    public void setVoterNif(String voterNif) {
        this.voterNif = voterNif;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }
}