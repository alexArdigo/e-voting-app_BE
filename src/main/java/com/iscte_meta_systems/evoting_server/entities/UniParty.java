package com.iscte_meta_systems.evoting_server.entities;
import com.iscte_meta_systems.evoting_server.enums.OrganisationType;
import jakarta.persistence.*;

@Entity
public class UniParty extends Organisation {

    private String name;
    private String imageUrl;
    @OneToOne(cascade = CascadeType.ALL)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "presidential_id")
    private Presidential presidential;

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

    public Presidential getPresidential() {
        return presidential;
    }

    public void setPresidential(Presidential presidential) {
        this.presidential = presidential;
    }

    @Override
    public OrganisationType getOrganisationType() {
        return OrganisationType.UNIPARTY;
    }
}
