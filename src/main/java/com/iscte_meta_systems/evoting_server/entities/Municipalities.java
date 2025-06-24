package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "municipalities")
public class Municipalities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String municipalityName;
    @ManyToOne
    @JoinColumn(name = "district_id")
    private Districts district;

    @OneToMany
    private List<Parishes> parishes;

    public Municipalities() {
    }

    public Municipalities(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public Districts getDistrict() {
        return district;
    }

    public void setDistrict(Districts district) {
        this.district = district;
    }

    public List<Parishes> getParishes() {
        return parishes;
    }

    public void setParishes(List<Parishes> parishes) {
        this.parishes = parishes;
    }
}