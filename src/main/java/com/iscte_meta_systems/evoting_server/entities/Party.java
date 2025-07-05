package com.iscte_meta_systems.evoting_server.entities;
import com.iscte_meta_systems.evoting_server.enums.OrganisationType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Party extends Organisation {

    private String name;
    private String color;
    private String logoUrl;
    private String description;
    @OneToMany
    List<Candidate> candidates;


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

    @Override
    public OrganisationType getOrganisationType() {
        return OrganisationType.PARTY;
    }
}
