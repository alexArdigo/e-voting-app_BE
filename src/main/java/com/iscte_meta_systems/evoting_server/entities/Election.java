package com.iscte_meta_systems.evoting_server.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Election {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @OneToMany
    private List<Organisation> organisations;
    @OneToMany
    List<Vote> votes;
    List<String> votersVoted; //HASHES
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
        if (this.organisations == null) {
            this.organisations = new java.util.ArrayList<>();
        }
        this.organisations.add(organisation);
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<String> getVotersVoted() {
        return votersVoted;
    }

    public void setVotersVoted(List<String> votersVoted) {
        this.votersVoted = votersVoted;
    }

    public void addVoted(String hashIdentification) {
        if (this.votersVoted == null) {
            this.votersVoted = new java.util.ArrayList<>();
        }
        this.votersVoted.add(hashIdentification);
    }


}
