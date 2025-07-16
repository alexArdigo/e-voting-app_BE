package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class VotingSession {

    @Id
    @GeneratedValue
    private Long id;

    private Long electionId;
    private Long voterId;
    private LocalDateTime expirationTime;
    private boolean isActive = false;

    public VotingSession() {
    }

    public VotingSession(Long electionId, Long voterId) {
        this.electionId = electionId;
        this.voterId = voterId;
        this.expirationTime = LocalDateTime.now().plusSeconds(300);
        this.isActive = true;
    }

    public Integer getRemainingTime() {
        return (int) java.time.Duration.between(LocalDateTime.now(), expirationTime).getSeconds();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
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

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
