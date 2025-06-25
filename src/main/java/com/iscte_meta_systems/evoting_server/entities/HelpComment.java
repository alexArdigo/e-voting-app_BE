package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class HelpComment {
    @Id
    @GeneratedValue
    private Long id;
    private String comment;

    @OneToOne
    private Answer answer;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
