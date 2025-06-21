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
}
