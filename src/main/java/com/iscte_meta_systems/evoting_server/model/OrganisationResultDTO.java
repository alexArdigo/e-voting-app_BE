package com.iscte_meta_systems.evoting_server.model;

public class OrganisationResultDTO {
    private Long organisationId;
    private String organisationName;
    private int votes;
    private double percentage;
    private int seats; // Para legislativas

    public OrganisationResultDTO() {}

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}