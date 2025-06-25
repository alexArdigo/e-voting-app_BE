package com.iscte_meta_systems.evoting_server.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Voter {

    @Id
    @GeneratedValue
    Long id;
    String hashIdentification;

    @OneToOne
    District district;

    @OneToOne
    Municipality municipality;

    @OneToOne
    Parish parish;

    public Voter() {
    }

    public Voter(
            String hashIdentification,
            District district,
            Municipality municipality,
            Parish parish
    ) {
        this.hashIdentification = hashIdentification;
        this.district = district;
        this.municipality = municipality;
        this.parish = parish;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashIdentification() {
        return hashIdentification;
    }

    public void setHashIdentification(String hashIdentification) {
        this.hashIdentification = hashIdentification;
    }

    public Parish getParish() {
        return parish;
    }

    public void setParish(Parish parish) {
        this.parish = parish;
    }
}
