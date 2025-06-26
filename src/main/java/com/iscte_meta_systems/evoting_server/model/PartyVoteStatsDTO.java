package com.iscte_meta_systems.evoting_server.model;

public class PartyVoteStatsDTO {

    private String partyName;
    private double percentage;

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

}
