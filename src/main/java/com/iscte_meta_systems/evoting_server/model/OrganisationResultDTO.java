package com.iscte_meta_systems.evoting_server.model;

import java.util.List;

public class OrganisationResultDTO {

    private String organisationName;
    private int votes;
    private double percentage;
    private int seats;
    private List<String> electedCandidates;
    private String color;

    public OrganisationResultDTO() {
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

    public List<String> getElectedCandidates() {
        return electedCandidates;
    }

    public void setElectedCandidates(List<String> electedCandidates) {
        this.electedCandidates = electedCandidates;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}