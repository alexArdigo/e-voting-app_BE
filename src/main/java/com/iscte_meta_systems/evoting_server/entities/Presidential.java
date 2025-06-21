package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Presidential extends Election {
    @OneToMany
    List<UniParty> candidates;

    public List<UniParty> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<UniParty> candidates) {
        this.candidates = candidates;
    }
}
