package com.iscte_meta_systems.evoting_server.entities;

import com.iscte_meta_systems.evoting_server.enums.VotingArea;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class ElectoralCircle {

    @Id
    @GeneratedValue
    private long id;
    @OneToOne
    District districts;
    private int seats;
    @OneToMany
    List<Party> parties;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
