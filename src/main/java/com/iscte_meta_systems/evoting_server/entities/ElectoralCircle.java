package com.iscte_meta_systems.evoting_server.entities;

import com.iscte_meta_systems.evoting_server.enums.VotingArea;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class ElectoralCircle {

    @Id
    @GeneratedValue
    private long id;
    private VotingArea votingArea;
    private int seats;
    @OneToMany
    List<Party> parties;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public VotingArea getVotingArea() {
        return votingArea;
    }

    public void setVotingArea(VotingArea votingArea) {
        this.votingArea = votingArea;
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
}
