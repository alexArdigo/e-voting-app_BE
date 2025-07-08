package com.iscte_meta_systems.evoting_server.entities;
import com.iscte_meta_systems.evoting_server.enums.OrganisationType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Party extends Organisation {

    private String name;
    private String color;
    private String logoUrl;
    private String description;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<Candidate> candidates;

    @ManyToOne
    @JoinColumn(name = "electoral_circle_id")
    private ElectoralCircle electoralCircle;

    public Party() {
    }

    public Party(String name, String color, String logoUrl, String description) {
        this.name = name;
        this.color = color;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Candidate> getCandidates() {return candidates;}

    public void setCandidates(List<Candidate> candidates) {this.candidates = candidates;}

    public ElectoralCircle getElectoralCircle() { return electoralCircle; }
    public void setElectoralCircle(ElectoralCircle electoralCircle) { this.electoralCircle = electoralCircle; }

    @Override
    public OrganisationType getOrganisationType() {
        return OrganisationType.PARTY;
    }
}
