package com.iscte_meta_systems.evoting_server.entities;

import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ElectoralCircle extends Election {

    @ManyToOne
    @JoinColumn(name = "legislative_id")
    private Legislative legislative;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District districts;

    @OneToMany(mappedBy = "electoralCircle", cascade = CascadeType.ALL)
    private List<Party> parties = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "electoral_circle_municipalities",
            joinColumns = @JoinColumn(name = "electoral_circle_id"),
            inverseJoinColumns = @JoinColumn(name = "municipality_id")
    )
    private List<Municipality> municipalities = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "electoral_circle_parishes",
            joinColumns = @JoinColumn(name = "electoral_circle_id"),
            inverseJoinColumns = @JoinColumn(name = "parish_id")
    )
    private List<Parish> parish = new ArrayList<>();

    private int seats;

    @Enumerated(EnumType.STRING)
    private ElectoralCircleType electoralCircleType;


    public ElectoralCircle() {
    }

    public ElectoralCircleType getElectoralCircleType() {
        return electoralCircleType;
    }

    public void setElectoralCircleType(ElectoralCircleType electoralCircleType) {
        this.electoralCircleType = electoralCircleType;
    }

    public List<Municipality> getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(List<Municipality> municipalities) {
        this.municipalities = municipalities;
    }

    public List<Parish> getParish() {
        return parish;
    }

    public void setParish(List<Parish> parish) {
        this.parish = parish;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public List<Party> getParties() {
        return parties;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
    }

    public District getDistricts() {
        return districts;
    }

    public void setDistricts(District district) {
        this.districts = district;
    }

    public Legislative getLegislative() {
        return legislative;
    }

    public void setLegislative(Legislative legislative) {
        this.legislative = legislative;
    }
}
