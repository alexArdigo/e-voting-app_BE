package com.iscte_meta_systems.evoting_server.model;

import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;

public class PartyVoteStatsDTO {

    private String partyName;
    private Long organisationId;
    private String organisationName;
    private ElectoralCircleType organisationType;
    private Integer votes;
    private double percentage;

    public PartyVoteStatsDTO(String partyName, double percentage) {
        this.partyName = partyName;
        this.percentage = percentage;
    }

    public PartyVoteStatsDTO() {
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public void setOrganisationType(ElectoralCircleType organisationType) {
        this.organisationType = organisationType;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getVotes() {
        return votes;
    }
}
