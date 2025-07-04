package com.iscte_meta_systems.evoting_server.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Organisation {

    @Id
    @GeneratedValue
    private Long id;

    private String organisationName;


    @ManyToOne
    @JsonBackReference
    private Election election;

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public Election getElection() {return election;}

    public void setElection(Election election) {this.election = election;}

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }
}
