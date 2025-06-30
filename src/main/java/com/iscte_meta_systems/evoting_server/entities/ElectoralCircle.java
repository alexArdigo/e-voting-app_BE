package com.iscte_meta_systems.evoting_server.entities;

import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import jakarta.persistence.*;
import org.springframework.data.geo.Circle;

import java.util.List;

@Entity
public class ElectoralCircle extends Election {

    @ManyToOne
    Legislative legislative;

    @OneToOne
    District districts;
    @OneToMany
    List<Party> parties;
    @OneToOne
    Municipality municipalities;
    @OneToOne
    Parish parish;
    private int seats;
    ElectoralCircleType electoralCircleType;


    public ElectoralCircle() {
    }

    public ElectoralCircleType getElectoralCircleType() {
        return electoralCircleType;
    }

    public void setElectoralCircleType(ElectoralCircleType electoralCircleType) {
        this.electoralCircleType = electoralCircleType;
    }

    public Municipality getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(Municipality municipalities) {
        this.municipalities = municipalities;
    }

    public Parish getParish() {
        return parish;
    }

    public void setParish(Parish parish) {
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

    public void setDistricts(District districts) {
        this.districts = districts;
    }

    public Legislative getLegislative() {
        return legislative;
    }

    public void setLegislative(Legislative legislative) {
        this.legislative = legislative;
    }
}
