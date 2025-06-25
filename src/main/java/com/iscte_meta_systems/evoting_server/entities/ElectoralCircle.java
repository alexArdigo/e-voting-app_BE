package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;
import org.springframework.data.geo.Circle;

import java.util.List;

@Entity
public class ElectoralCircle extends Election {


    @OneToOne
    District districts;
    private int seats;
    @OneToMany
    List<Party> parties;

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
}
