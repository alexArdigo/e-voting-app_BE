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
    ElectoralCircle electoralCircle;

    @OneToOne
    Parishes parish;

    public Voter() {
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

    public ElectoralCircle getElectoralCircle() {
        return electoralCircle;
    }

    public void setElectoralCircle(ElectoralCircle electoralCircle) {
        this.electoralCircle = electoralCircle;
    }

    public Parishes getParish() {
        return parish;
    }

    public void setParish(Parishes parish) {
        this.parish = parish;
    }
}
