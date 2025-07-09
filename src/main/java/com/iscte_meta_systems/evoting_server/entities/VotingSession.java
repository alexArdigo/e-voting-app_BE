package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;

@Entity
public class VotingSession {

    @Id
    @GeneratedValue
    private Long id;

    private Long electionId;
    private Long voterId;

    public VotingSession() {
    }

    public VotingSession(Long electionId, Long voterId) {
        this.electionId = electionId;
        this.voterId = voterId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVoterId() {
        return voterId;
    }

    public void setVoterId(Long voterId) {
        this.voterId = voterId;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }
}
