package com.iscte_meta_systems.evoting_server.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.iscte_meta_systems.evoting_server.enums.OrganisationType;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Organisation {

    @Id
    @GeneratedValue
    private Long id;
    private String organisationName;
    @NotNull
    @Enumerated(EnumType.STRING)
    private OrganisationType organisationType;


    @ManyToOne
    @JoinColumn(name = "election_id")
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
