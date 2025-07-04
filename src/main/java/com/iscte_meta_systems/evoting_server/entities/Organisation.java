package com.iscte_meta_systems.evoting_server.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.iscte_meta_systems.evoting_server.enums.OrganisationType;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Organisation {
    @Id
    @GeneratedValue
    private Long id;

    private String organisationName;

    @ManyToOne
    private Election election;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    // Adicione este método abstrato
    public abstract OrganisationType getOrganisationType();
}