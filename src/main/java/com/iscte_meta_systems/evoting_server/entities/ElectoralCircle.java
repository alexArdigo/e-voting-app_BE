package com.iscte_meta_systems.evoting_server.entities;

import com.iscte_meta_systems.evoting_server.enums.VotingArea;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class ElectoralCircle {

    @Id
    @GeneratedValue
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "voting_area")
    private VotingArea votingArea;
    @OneToMany
    @JoinColumn(name = "electoral_circle_id")
    private List<Districts> districts;
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

    public List<Districts> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Districts> districts) {
        this.districts = districts;
    }
}
