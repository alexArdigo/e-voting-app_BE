package com.iscte_meta_systems.evoting_server.model;

import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;

import java.util.List;

public class ElectionDTO {

    private ElectionType electionType;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<OrganisationDTO> organisations;
    private String districtName;
    private Integer seats;
    private String municipalityName;
    private String parishName;
    private ElectoralCircleType electoralCircleType;
    private Long legislativeId;
    private Long id;
    private boolean isStarted;

    public ElectionType getElectionType() {
        return electionType;
    }

    public void setElectionType(ElectionType electionType) {
        this.electionType = electionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<OrganisationDTO> getOrganisations() {
        return organisations;
    }

    public void setOrganisations(List<OrganisationDTO> organisations) {
        this.organisations = organisations;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getParishName() {
        return parishName;
    }

    public void setParishName(String parishName) {
        this.parishName = parishName;
    }

    public ElectoralCircleType getElectoralCircleType() {
        return electoralCircleType;
    }

    public void setElectoralCircleType(ElectoralCircleType electoralCircleType) {
        this.electoralCircleType = electoralCircleType;
    }

    public Long getLegislativeId() {
        return legislativeId;
    }

    public void setLegislativeId(Long legislativeId) {
        this.legislativeId = legislativeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isStarted() {
        return isStarted;
    }
    public void setStarted(boolean started) {
        isStarted = started;
    }
}
