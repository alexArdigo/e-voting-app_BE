package com.iscte_meta_systems.evoting_server.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Vote {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Organisation organisation;
    @OneToOne
    private Parishes parish;


    public Vote() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Parishes getParish() {
        return parish;
    }

    public void setParish(Parishes parish) {
        this.parish = parish;
    }
}