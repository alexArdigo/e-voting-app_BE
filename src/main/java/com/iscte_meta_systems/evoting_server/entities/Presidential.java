package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Presidential extends Election {
    @OneToMany(mappedBy = "presidential", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniParty> candidates = new ArrayList<>();

    public List<UniParty> getCandidates() { return candidates; }
    public void setCandidates(List<UniParty> candidates) { this.candidates = candidates; }

    public void addCandidate(UniParty candidate) {
        candidates.add(candidate);
    }
}
