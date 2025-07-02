package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;

@Entity
public class VoterHash {
    @Id
    @GeneratedValue
    private Long id;

    private String voterHash;

    @ManyToOne
    @JoinColumn(name = "election_id")
    private Election election;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVoterHash() {
        return voterHash;
    }

    public void setVoterHash(String voterHash) {
        this.voterHash = voterHash;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }
}

