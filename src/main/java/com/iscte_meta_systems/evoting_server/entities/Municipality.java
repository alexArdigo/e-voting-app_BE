package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "municipalities")
public class Municipality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String municipalityName;
    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @OneToMany
    private List<Parish> parishes;

    public Municipality() {
    }

    public Municipality(String municipalityName) {
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

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public List<Parish> getParishes() {
        return parishes;
    }

    public void setParishes(List<Parish> parishes) {
        this.parishes = parishes;
    }
}