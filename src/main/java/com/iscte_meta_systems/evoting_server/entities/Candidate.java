package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Candidate {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String imageUrl;

}
