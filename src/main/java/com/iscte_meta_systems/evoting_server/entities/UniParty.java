package com.iscte_meta_systems.evoting_server.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class UniParty extends Organisation {

    private String name;
    private String imageUrl;
    @OneToOne
    Candidate candidate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

}
