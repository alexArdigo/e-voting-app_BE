package com.iscte_meta_systems.evoting_server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Election {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ElectionType type;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @JsonIgnore
    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Organisation> organisations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoterHash> votersVoted = new ArrayList<>();

    boolean started = false;

    public Election() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void startElection() {
        this.started = true;
    }

    public void endElection() {
        this.started = false;
    }

    public List<Organisation> getOrganisations() {
        return organisations;
    }

    public void setOrganisations(List<Organisation> organisations) {
        this.organisations = organisations;
    }

    public void addVote(Vote vote) {
        this.votes.add(vote);
    }

    public void addOrganisation(Organisation organisation) {
        this.organisations.add(organisation);
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<VoterHash> getVotersVoted() {
        return votersVoted;
    }

    public void setVotersVoted(List<VoterHash> votersVoted) {
        this.votersVoted = votersVoted;
    }

    public ElectionType getType() {
        return type;
    }

    public void setType(ElectionType type) {
        this.type = type;
    }
}
