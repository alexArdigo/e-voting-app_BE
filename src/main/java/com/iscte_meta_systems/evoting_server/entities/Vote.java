package com.iscte_meta_systems.evoting_server.entities;


import jakarta.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Organisation organisation;
    @OneToOne
    private Municipality municipality;
    @OneToOne
    private Parish parish;

    public Vote() {
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
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

    public Parish getParish() {
        return parish;
    }

    public void setParish(Parish parish) {
        this.parish = parish;
    }

}